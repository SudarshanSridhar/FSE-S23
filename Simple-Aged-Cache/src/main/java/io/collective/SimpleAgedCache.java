package io.collective;

import java.time.Clock;

public class SimpleAgedCache{
    private ExpirableEntry[] entries;
    private int size = 0;
    private Clock clock = null;

    class ExpirableEntry{
        private Object key;
        private Object value;
        private long retentionInMillis;
        private long creationTime;

        public boolean isExpired(){
            return false;
        }

        public ExpirableEntry(Object key, Object value, int retentionInMillis){
            this.key = key;
            this.value = value;
            this.retentionInMillis = retentionInMillis;
            this.creationTime = System.currentTimeMillis();
        }

        public long getRetentionTime(){
            return this.retentionInMillis;
        }

        public long getCreationTime(){
            return this.creationTime;
        }

        public Object getKey(){
            return this.key;
        }

        public Object getValue(){
            return this.value;
        }
    }

    public SimpleAgedCache(Clock clock) {
        this();
        this.clock = clock;
    }

    public SimpleAgedCache() {
        entries = new ExpirableEntry[10];
    }

    public void put(Object key, Object value, int retentionInMillis) {
        entries[size++] = new ExpirableEntry(key, value, retentionInMillis);
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private void removeEntryAt(int index){
        entries[index] = entries[size - 1];
        entries[size - 1] = null;
        size--;
    }

    private int getCountOfValidEntries(Clock clock){
        for(int i = 0; i < size; i++){
            if(clock.millis() > entries[i].getCreationTime() + entries[i].getRetentionTime()){
                removeEntryAt(i--);
            }
        }
        return size;
    }

    private int getCountOfValidEntries(){
        return size;
    }

    public int size() {
        if(clock == null){
            return getCountOfValidEntries();
        }
        return getCountOfValidEntries(clock);
    }

    public Object get(Object key) {
        for(int i = 0; i < 10; i++){
            if(entries[i] != null && 
                entries[i].getKey().toString().compareTo(key.toString()) == 0){
                return entries[i].getValue();
            }
        }
        return null;
    }
}