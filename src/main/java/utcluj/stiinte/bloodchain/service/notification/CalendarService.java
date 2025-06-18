package utcluj.stiinte.bloodchain.service.notification;

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
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.donation.PeriodicDonation;
import utcluj.stiinte.bloodchain.model.location.Address;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class CalendarService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
    private static final String CREDENTIALS_FILE_PATH = "google/credentials.json";
    private static final String CALENDAR_ID = "primary";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("YYYYmmdd'T'HHmmss'Z'");

    public void createPeriodicEvent(PeriodicDonation periodicDonation) {
        Event event = new Event()
                .setSummary("Donate blood")
                //.setLocation(formatAddress(periodicDonation.getTransfusionCenter().getAddress()))
                .setDescription("Periodic reminder from the transfusion center")
                .setAttendees(List.of(new EventAttendee().setEmail(periodicDonation.getDonor().getEmail())))
                .setStart(getEventDateTime(periodicDonation.getStartDate(), periodicDonation.getTime()))
                .setEnd(getEventDateTime(periodicDonation.getEndDate(), periodicDonation.getTime()))
                .setReminders(new Event.Reminders()
                        .setUseDefault(false)
                        .setOverrides(List.of(new EventReminder().setMethod("email").setMinutes(24 * 60))))
                .setRecurrence(List.of(String.format("RRULE:FREQ=MONTHLY;INTERVAL=%s;UNTIL=%s;BYDAY=MO)))",
                        periodicDonation.getFrequencyPerYear(),
                        DATE_TIME_FORMATTER.format(periodicDonation.getEndDate()))));

        try {
            getCalendar().events().insert(CALENDAR_ID, event).execute();
        } catch (IOException | GeneralSecurityException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Calendar getCalendar() throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
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

    private EventDateTime getEventDateTime(LocalDate date, LocalTime time) {
        return new EventDateTime().setDateTime(new DateTime(LocalDateTime.of(date, time).toString())).setTimeZone("Europe/Bucharest");
    }
}