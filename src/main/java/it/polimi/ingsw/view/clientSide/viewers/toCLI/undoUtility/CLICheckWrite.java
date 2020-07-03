//package it.polimi.ingsw.view.clientSide.viewers.toCLI.undoUtility;
//
///**
// * Classes uses to control the first write on a scanner
// *
// * @author Marco
// */
//public class CLICheckWrite {
//
//    private boolean written;
//
//    /**
//     * Constructor which sets written at false
//     */
//    public CLICheckWrite() {
//        written = false;
//    }
//
//    /**
//     * synchronized method which checks the parameter written and set it at true and return true if it is false or
//     * return false if it is true. This method and class are used when some threads want to write on a same Scanner and
//     * they want to know if one of them had written, for example to interrupt the Scanner
//     *
//     * @return true if written == false, false if written == true
//     */
//    public synchronized boolean firstToWrite() {
//        if ( !written ) {
//            written = true;
//            return true;
//        }
//
//        return false;
//    }
//}
