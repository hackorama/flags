package com.hackorama.flags.data;

import java.util.List;
import java.util.Set;

/**
 * Data store interface
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public interface DataStore {

    /**
     * Clear all data in the store
     */
    public void clear();

    /**
     * Close the data store and free all resources
     */
    public void close();

    /**
     * Check if a keys exists in the named store
     *
     * @param store The store name
     * @param key   The key to search for
     * @return True if the named store contains the key
     */
    public boolean contains(String store, String key);

    /**
     * Get all values from the named store
     *
     * @param store The store name
     * @return All keys from the named store
     */
    public List<String> get(String store);

    /**
     * Get the value for a key from the named store
     *
     * @param store The store name
     * @param key   The key
     * @return The value for the given key from the named store
     */
    public String get(String store, String key);

    /**
     * Get the keys for a given value from the named store
     *
     * @param store The store name
     * @param value The value to search for
     * @return List of all keys that maps to given value in the named store
     */
    public List<String> getByValue(String store, String value);

    /**
     * Get all keys from the named store
     *
     * @param store The store name
     * @return List of all keys from the named store
     */
    public Set<String> getKeys(String store);

    /**
     * Get the values for a key from the named multimap store
     *
     * @param store The multimap store name
     * @param key   The key to search for
     * @return List of all values that maps to the key in the named multimap store
     */
    public List<String> getMultiKey(String store, String key);

    /**
     * Insert a new key value pair into the named store
     *
     * @param store The store name
     * @param key   The key to insert
     * @param value The value to insert
     */
    public void put(String store, String key, String value);

    /**
     * Insert a new key value pair into the named multimap store
     *
     * @param store The multimap store name
     * @param key   The key to insert
     * @param value The value to insert
     */
    public void putMultiKey(String store, String key, String value);

    /**
     * Remove value(s) for the given key from the named store
     *
     * @param store The store name
     * @param key   The key to remove value(s)
     */
    public void remove(String store, String key);

    /**
     * Remove the given key and value from the named multimap store
     *
     * @param store The named multimap store
     * @param key   The key to remove
     * @param value The value to remove
     */
    public void remove(String store, String key, String value);

}
