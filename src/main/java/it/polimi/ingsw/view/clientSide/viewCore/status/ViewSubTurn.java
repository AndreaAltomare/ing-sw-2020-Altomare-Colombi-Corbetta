package it.polimi.ingsw.view.clientSide.viewCore.status;

import it.polimi.ingsw.view.clientSide.viewCore.data.dataClasses.ViewNickname;
import it.polimi.ingsw.view.clientSide.viewCore.interfaces.ClientAddressable;
import it.polimi.ingsw.view.clientSide.viewers.SubTurnViewer;
import it.polimi.ingsw.view.exceptions.NotFoundException;
import it.polimi.ingsw.view.interfaces.Addressable;

public enum ViewSubTurn implements ClientAddressable {
    SELECTWORKER("SELECTWORKER"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SubTurnViewer() {
                @Override
                public Object toTerminal() {
                    return null;
                }

                @Override
                public Object toGUI() {
                    return null;
                }

                @Override
                public Object toCLI() {
                    return null;
                }
            };
        }
    },
    BUILD("BUILD"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SubTurnViewer() {
                @Override
                public Object toTerminal() {
                    return null;
                }

                @Override
                public Object toGUI() {
                    return null;
                }

                @Override
                public Object toCLI() {
                    return null;
                }
            };
        }
    },
    MOVE("MOVE"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SubTurnViewer() {
                @Override
                public Object toTerminal() {
                    return null;
                }

                @Override
                public Object toGUI() {
                    return null;
                }

                @Override
                public Object toCLI() {
                    return null;
                }
            };
        }
    },
    BUILDORMOVE("BUILDORMOVE"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SubTurnViewer() {
                @Override
                public Object toTerminal() {
                    return null;
                }

                @Override
                public Object toGUI() {
                    return null;
                }

                @Override
                public Object toCLI() {
                    return null;
                }
            };
        }
    },
    CANBUILD("CANBUILD"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SubTurnViewer() {
                @Override
                public Object toTerminal() {
                    return null;
                }

                @Override
                public Object toGUI() {
                    return null;
                }

                @Override
                public Object toCLI() {
                    return null;
                }
            };
        }
    },
    CANMOVE("CANMOVE"){
        @Override
        public SubTurnViewer getSubViewer() {
            return new SubTurnViewer() {
                @Override
                public Object toTerminal() {
                    return null;
                }

                @Override
                public Object toGUI() {
                    return null;
                }

                @Override
                public Object toCLI() {
                    return null;
                }
            };
        }
    };

    private static ViewSubTurn actualSubTurn = null;
    private static String player;
    private String subTurn;

    ViewSubTurn(String name){
        subTurn = name;
    }

    public static String getClassId(){ return "[SubTurn]"; }

    @Override
    public String getMyClassId() { return getClassId(); }

    @Override
    public String getId() { return subTurn; }

    @Override
    public String toString(){ return getClassId() + "\t" + getId(); }

    @Override
    public boolean equals(Addressable pl) { return isThis(pl.toString()); }

    @Override
    public boolean isThis(String st) { return this.toString().equals(st); }

    public static ViewSubTurn search(String st) throws NotFoundException {
        for (ViewSubTurn i: ViewSubTurn.values() ) {
            if(i.isThis(st))
                return i;
        }
        throw new NotFoundException();
    }

    public static void setSubTurn(ViewSubTurn subTurn){ actualSubTurn = subTurn; }

    public static void set(String st){
        try {
            setSubTurn(search(st));
        } catch (NotFoundException e) {
            setSubTurn(null);
        }
    }

    public static ViewSubTurn getActual(){ return actualSubTurn; }

    public void setPlayer(String player){ this.player = player; }

    public boolean isMyTurn(){return ViewNickname.getNickname().isThis(player); }

    public abstract SubTurnViewer getSubViewer();
}
