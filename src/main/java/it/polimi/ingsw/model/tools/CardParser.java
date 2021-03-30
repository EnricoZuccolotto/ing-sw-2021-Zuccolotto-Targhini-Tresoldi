package it.polimi.ingsw.model.tools;

import com.google.gson.*;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.enums.Advantages;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CardParser {




    private CardParser() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parse all the weapons from DevCards.json
     *
     * @return a set of decks of all the DevCard
     */

    public static Deck[][] parseDevCards() {
        Deck [][]decks= new Deck[4][3];
        for(int i=0;i<4;i++)
            for(int j=0;j<3;j++)
                decks[i][j]=new Deck();

        InputStream is = CardParser.class.getClassLoader().getResourceAsStream("Json/DevCards.json");

            if (is == null) {
                System.out.println("Error");
                return null;
            }

        JsonParser parser;
        parser = new JsonParser();

        JsonObject json = parser.parse(new InputStreamReader(is)).getAsJsonObject();
        JsonArray DevCards = json.getAsJsonArray("DevCards");

        for (JsonElement DevelCard : DevCards) {
            JsonObject DevCard = DevelCard.getAsJsonObject();


            //String imagePath = .get("image").getAsString();
            int ID = DevCard.get("ID").getAsInt();
            int VP = DevCard.get("VP").getAsInt();
            JsonArray tmp = DevCard.get("Cost").getAsJsonArray();
            int[] Cost = parseIntJsonArray(tmp);
            tmp = DevCard.get("ProductionCost").getAsJsonArray();
            int[] ProductionCost = parseIntJsonArray(tmp);
            tmp = DevCard.get("ProductionResult").getAsJsonArray();
            int[] ProductionResult = parseIntJsonArray(tmp);
            String tmp1=DevCard.get("Color").getAsString();
            Colors c=Colors.valueOf(tmp1);
            int lvl = DevCard.get("Level").getAsInt();
            // Card creation
            decks[c.ordinal()][lvl-1].addCard(new DevelopmentCard(VP,ID, Cost,ProductionCost,ProductionResult,c,lvl));
        }
        for(int i=0;i<4;i++)
            for(int j=0;j<3;j++)
                decks[i][j].shuffle();
        return decks;
    }

    public static List<LeaderCard> parseLeadCards() {

        List<LeaderCard> deck= new ArrayList<>();
        InputStream is = CardParser.class.getClassLoader().getResourceAsStream("Json/LeaderCards.json");

        if (is == null) {
            System.out.println("Error");
            return null;
        }

        JsonParser parser;
        parser = new JsonParser();

        JsonObject json = parser.parse(new InputStreamReader(is)).getAsJsonObject();
        JsonArray LeadCards = json.getAsJsonArray("LeaderCards");

        for (JsonElement LeadCard : LeadCards) {
            JsonObject DevCard = LeadCard.getAsJsonObject();


            //String imagePath = .get("image").getAsString();
            int ID = DevCard.get("ID").getAsInt();
            int VP = DevCard.get("VP").getAsInt();
            JsonArray tmp = DevCard.get("costResources").getAsJsonArray();
            int[] costResources = parseIntJsonArray(tmp);
            tmp = DevCard.get("costColor").getAsJsonArray();
            int[] costColor = parseIntJsonArray(tmp);
            String tmp1=DevCard.get("advantage").getAsString();
            Advantages a= Advantages.valueOf(tmp1);
            tmp = DevCard.get("effect1").getAsJsonArray();
            int[] effect1 = parseIntJsonArray(tmp);
            tmp = DevCard.get("effect2").getAsJsonArray();
            int[] effect2 = parseIntJsonArray(tmp);


            // Card creation
            deck.add(new LeaderCard(VP,ID, costResources,costColor,a,effect1,effect2));
        }

        return deck;
    }

    static int[] parseIntJsonArray(JsonArray jsonArray) {
        List<Integer> list = new ArrayList<>();

        for (JsonElement elem : jsonArray) {
            list.add(elem.getAsInt());
        }

        return list.stream().mapToInt(i -> i).toArray();
    }



}
