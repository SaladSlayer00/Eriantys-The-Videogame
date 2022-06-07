package it.polimi.ingsw.model.expertDeck;

import java.util.ArrayList;

public class ExpertCards {
    private ArrayList<Character> cards;
    public ExpertCards(){
        cards = new ArrayList<>();
        //cards.add(new ExchangeStudentsCard());
        cards.add(new ImproperInfluenceCard());
        cards.add(new InfluenceBansCard());
        cards.add(new NoTowerCard());
        cards.add(new NullColorCard());
        //cards.add(new OneMoreStudentCard());
        cards.add(new ProfessorControllerCard());
        cards.add(new RemoveAColorCard());
        cards.add(new SwapTwoStudentsCard());
        //cards.add(new ToTheDashboardCard());
        cards.add(new TwoMoreMovesCard());
        cards.add(new TwoMorePointsCard());
    }

    public ArrayList<Character> getCards() {
        return cards;
    }
}
