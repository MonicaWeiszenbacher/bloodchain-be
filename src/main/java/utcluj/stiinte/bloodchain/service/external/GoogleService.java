package utcluj.stiinte.bloodchain.service.external;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.donation.PeriodicDonation;
import utcluj.stiinte.bloodchain.model.location.Address;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

@Service
public class GoogleService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR_EVENTS, GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "google/credentials.json";
    private static final String CALENDAR_ID = "primary";
    private static final String GMAIL_ID = "me";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    public void createPeriodicEvent(PeriodicDonation periodicDonation) {
        Event event = new Event()
                .setSummary("Donate blood")
                .setLocation(formatAddress(periodicDonation.getTransfusionCenter().getAddress()))
                .setDescription("Periodic reminder from the transfusion center")
                .setAttendees(List.of(new EventAttendee().setEmail(periodicDonation.getDonor().getEmail())))
                .setStart(getEventDateTime(periodicDonation.getStartDate()))
                .setEnd(getEventDateTime(periodicDonation.getEndDate()))
                .setReminders(new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(List.of(new EventReminder().setMethod("email").setMinutes(24 * 60))))
                .setRecurrence(List.of(String.format("RRULE:FREQ=MONTHLY;INTERVAL=%s;UNTIL=%s;BYDAY=MO",
                        periodicDonation.getFrequencyPerYear(),
                        DATE_TIME_FORMATTER.format(periodicDonation.getEndDate().atZone(ZoneId.systemDefault())))));

        try {
            getCalendar().events().insert(CALENDAR_ID, event).execute();
        } catch (IOException | GeneralSecurityException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void sendAppointmentEmail(Donation donation) {
        try {
            String messageSubject = "Blood donation appointment";
            String bodyText = String.format("You have an upcoming blood donation. Location: %s. Time: %s",
                    formatAddress(donation.getTransfusionCenter().getAddress()),
                    donation.getTime());

            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);
            email.setFrom(new InternetAddress(donation.getTransfusionCenter().getEmail()));
            email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(donation.getDonor().getEmail()));
            email.setSubject(messageSubject);
            email.setText(bodyText);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            byte[] rawMessageBytes = buffer.toByteArray();
            String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
            Message message = new Message();
            message.setRaw(encodedEmail);
            getGmail().users().messages().send(GMAIL_ID, message).execute();
        } catch (MessagingException | IOException | GeneralSecurityException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Calendar getCalendar() throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName("Bloodchain")
                .build();
    }

    private Gmail getGmail() throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Gmail.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName("Bloodchain")
                .build();
    }

    private Credential getCredentials(NetHttpTransport httpTransport)
            throws IOException {
        InputStream credentialsFile = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();

        if (credentialsFile == null) {
            throw new AppException(HttpStatus.FORBIDDEN, "google_api_credentials_not_found");
        }

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(credentialsFile));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private String formatAddress(Address address) {
        return StringUtils.join(",", address.getStreet(), address.getCity().getName(), address.getCity().getCountry());
    }

    private EventDateTime getEventDateTime(LocalDateTime dateTime) {
        return new EventDateTime().setDateTime(new DateTime(dateTime.atOffset(ZoneOffset.UTC).toString())).setTimeZone("Europe/Bucharest");
    }
}