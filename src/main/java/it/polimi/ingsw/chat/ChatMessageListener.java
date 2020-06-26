package it.polimi.ingsw.chat;

/**
 * Listener interface for ChatMessage events.
 *
 * @author AndreaAltomare
 */
public interface ChatMessageListener {

    /**
     * Update Listener with a received chat message.
     *
     * @param chatMessage Chat message
     */
    public void update(ChatMessageEvent chatMessage);
}
