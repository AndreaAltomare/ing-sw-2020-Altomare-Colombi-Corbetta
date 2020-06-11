package it.polimi.ingsw.chat;

/**
 * Listener interface for ChatMessage events.
 *
 * @author AndreaAltomare
 */
public interface ChatMessageListener {
    public void update(ChatMessageEvent chatMessage);
}
