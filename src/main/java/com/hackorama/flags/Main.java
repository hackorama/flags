package com.hackorama.flags;

import java.sql.SQLException;

import com.hackorama.flags.common.Util;
import com.hackorama.flags.data.MemoryDataStore;
import com.hackorama.flags.data.jdbc.JDBCDataStore;
import com.hackorama.flags.data.mapdb.MapdbDataStore;
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
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        usage(args);
        if (Util.usingJDBC(args)) {
            new FlagService().configureUsing(new SpringServer("Flag Service Using H2 JDBC Store"))
                    .configureUsing(new JDBCDataStore()).start();
        } else if (Util.usingMapDB(args)) {
            new FlagService().configureUsing(new SpringServer("Flag Service Using MapDB Key Value Store"))
                    .configureUsing(new MapdbDataStore()).start();
        } else {
            new FlagService().configureUsing(new SpringServer("Flag Service Using In Memory Store"))
                    .configureUsing(new MemoryDataStore()).start();
        }
    }

    private static void usage(String[] args) {
        if (args.length > 0 && ("-h".equals(args[0]) || "--h".equals(args[0]) || "-help".equals(args[0])
                || "--help".equals(args[0]) || "help".equals(args[0]))) {
            System.out.println("Flag Service");
            System.out.println("");
            System.out.println("Usage : java Main [-jdbc] [-mapdb] [-h]");
            System.out.println("   -jdbc  Use H2 JDBC Data Store");
            System.out.println("   -mapdb Use Mapdb Key Value Data Store");
            System.out.println("   -h     Print this help");
            System.exit(0);
        }
    }

}
