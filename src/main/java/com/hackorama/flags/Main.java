package com.hackorama.flags;

import com.hackorama.flags.data.MemoryDataStore;
import com.hackorama.flags.server.spring.SpringServer;
import com.hackorama.flags.service.flag.FlagService;

/**
 * Main application entry point
 *
 * @author Kishan Thomas (kishan.thomas@gmail.com)
 *
 */
public class Main {

    /**
     * Main application entry point
     *
     * @param args Configuration options
     */
    public static void main(String[] args) {
        usage(args);
        new FlagService().configureUsing(new SpringServer("Flag Search Service")).configureUsing(new MemoryDataStore())
                .start();
    }

    private static void usage(String[] args) {
        if (args.length > 0 && ("-h".equals(args[0]) || "--h".equals(args[0]) || "-help".equals(args[0])
                || "--help".equals(args[0]) || "help".equals(args[0]))) {
            System.out.println("Flag service");
            System.out.println("Usage : Main ");
            System.exit(0);
        }
    }

}
