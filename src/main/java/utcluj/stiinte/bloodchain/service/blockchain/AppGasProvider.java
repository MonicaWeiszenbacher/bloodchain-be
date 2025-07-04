package utcluj.stiinte.bloodchain.service.blockchain;

import org.springframework.stereotype.Component;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

@Component
public class AppGasProvider implements ContractGasProvider {
    
    private static final BigInteger GAS_PRICE = new BigInteger("20000000000"); // 20,000,000,000 Wei
    
    private static final BigInteger GAS_LIMIT = new BigInteger("6721975"); // Example: 6,721,975 units

    @Override
    public BigInteger getGasPrice() {
        return GAS_PRICE;
    }

    @Override
    public BigInteger getGasLimit(org.web3j.protocol.core.methods.request.Transaction transaction) {
        return GAS_LIMIT;
    }

    @Override
    public BigInteger getGasLimit() {
        return GAS_LIMIT;
    }
}