package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Communication.CommunicationMessage;
import it.polimi.ingsw.model.FaithPath;
import it.polimi.ingsw.model.Market;
import it.polimi.ingsw.model.cards.Decks;
import it.polimi.ingsw.model.modelsToSend.CompressedPlayerBoard;
import it.polimi.ingsw.observer.ViewObservable;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class Gui extends ViewObservable implements View {
    @Override
    public void askUsername() {

    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void joinLobby() {

    }

    @Override
    public void askFirstAction() {

    }

    @Override
    public void askSecondAction() {

    }

    @Override
    public void askGetMarket() {

    }

    @Override
    public void askSortingMarket() {

    }

    @Override
    public void askSwitchRows() {

    }

    @Override
    public void askGetProduction() {

    }

    @Override
    public void askUseBaseProduction() {

    }

    @Override
    public void askUseSpecialProduction() {

    }

    @Override
    public void askUseNormalProduction() {

    }

    @Override
    public void askFoldLeader() {

    }

    @Override
    public void askActiveLeader() {

    }

    @Override
    public void showPlayerBoard(CompressedPlayerBoard playerBoard) {

    }

    @Override
    public void showFaithPath(FaithPath faithPath) {

    }

    @Override
    public void showDecks(Decks decks) {

    }

    @Override
    public void showMarket(Market market) {

    }

    @Override
    public void showLoginResult(boolean nick, boolean accepted, String name) {

    }

    @Override
    public void showLobby(ArrayList<String> players) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showCommunication(String communication, CommunicationMessage type) {

    }
}
