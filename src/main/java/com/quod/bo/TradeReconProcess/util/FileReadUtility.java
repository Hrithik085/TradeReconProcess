package com.quod.bo.TradeReconProcess.util;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author - tra865
 * @date - 02-05-2022
 */

public class FileReadUtility {
    public static BufferedReader sftpService(String fileName) {

        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession("root", "192.168.39.171", 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("Fingtl@123");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            return fileReader(sftpChannel, fileName);
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedReader fileReader(ChannelSftp sftpChannel, String fileName) throws SftpException {
        String line = "";
        InputStream stream = sftpChannel.get(fileName);

        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            return reader;
        } catch (Exception e) {
            System.out.println("Exception occurred during reading file from SFTP server due to " + e.getMessage());
            e.getMessage();

        }
        return null;
    }
}
