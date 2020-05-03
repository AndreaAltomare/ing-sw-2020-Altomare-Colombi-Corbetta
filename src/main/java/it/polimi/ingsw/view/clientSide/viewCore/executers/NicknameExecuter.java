package it.polimi.ingsw.view.clientSide.viewCore.executers;

public class NicknameExecuter implements Executer {

    private String nickname;

    @Override
    public void doIt() {
        ;
    }

    @Override
    public String exType() {
        return myType();
    }

    public static String myType(){
        return "[Executer]" + "\t" + "Nickname";
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public static boolean isSameType(Executer ex){
        return (ex.exType().equals(myType()));
    }
}
