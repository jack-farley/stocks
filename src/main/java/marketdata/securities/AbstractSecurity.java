package marketdata.securities;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractSecurity implements Security {

    private final ReadWriteLock securityLock = new ReentrantReadWriteLock();
    protected SecurityType type;
    protected String ticker;
    protected BigDecimal price;

    @Override
    public String getTicker(){
        Lock readLock = securityLock.readLock();
        readLock.lock();

        try {
            return this.ticker;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void updatePrice(BigDecimal newPrice) {
        Lock writeLock = securityLock.writeLock();
        writeLock.lock();

        try {
            this.price = newPrice;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public BigDecimal getPrice() {
        Lock readLock = securityLock.readLock();
        readLock.lock();

        try {
            return this.price;
        } finally {
            readLock.unlock();
        }
    }
}
