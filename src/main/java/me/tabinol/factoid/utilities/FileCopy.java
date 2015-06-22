/*
 Factoid: Lands and Factions plugin for Minecraft server
 Copyright (C) 2014 Kaz00, Tabinol

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.tabinol.factoid.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

// TODO Auto-generated Javadoc
/**
 * The Class FileCopy.
 */
public class FileCopy {

    /**
     * Copy text from jar.
     *
     * @param in the file from (FileInputStream)
     * @param fileTo the file to (File)
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void copyTextFromJav(InputStream in, File fileTo) throws IOException {

        Scanner scan = new Scanner(in, "UTF8");
        OutputStream out = new FileOutputStream(fileTo);
        BufferedWriter outbw = new BufferedWriter(new OutputStreamWriter(out));
        
        while (scan.hasNext()) {
            outbw.write(scan.nextLine());
            outbw.newLine();
        }
        
        outbw.close();
        scan.close();
    }
}
