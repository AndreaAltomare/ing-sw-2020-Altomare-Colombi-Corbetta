package it.polimi.ingsw.view.clientSide.viewers.messages;

import it.polimi.ingsw.chat.ChatMessageEvent;

import java.util.ArrayList;

public class PlayerMessages {

    private static ArrayList<String[]> messageList = new ArrayList<String[]>();
    private static boolean notify = false;

    public static void addMsg(ChatMessageEvent msg){
        String[] payload = new String[2];
        payload[0] = msg.getSender();
        payload[1] = msg.getMessage();
        messageList.add(payload);
    }

    public static int getLen(){
        return messageList.size();
    }

    public static String[][] retrive(int n, int offset){
        String[][] ret = new String[n][2];
        int m = Math.min(offset+n, getLen());
        if(offset>getLen())
            return null;

        for(int i = offset; i<m; i++){
            ret[i-offset][0] = messageList.get(i)[0];
            ret[i-offset][1] = messageList.get(i)[1];
        }

        return ret;
    }
}
