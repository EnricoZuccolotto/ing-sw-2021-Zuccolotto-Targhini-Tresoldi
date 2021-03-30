package it.polimi.ingsw.model.tools;

import com.google.gson.*;
import it.polimi.ingsw.model.enums.Colors;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class DevelopmentCardParser {




    private DevelopmentCardParser() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parse all the weapons from weapons.json
     *
     * @return a deck of all the WeaponCard
     */

    public static Deck[][] parseCards() {
        Deck [][]decks= new Deck[4][3];
        for(int i=0;i<4;i++)
            for(int j=0;j<3;j++)
                decks[i][j]=new Deck();

        InputStream is = DevelopmentCardParser.class.getClassLoader().getResourceAsStream("Json/Cards.json");

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


    static int[] parseIntJsonArray(JsonArray jsonArray) {
        List<Integer> list = new ArrayList<>();

        for (JsonElement elem : jsonArray) {
            list.add(elem.getAsInt());
        }

        return list.stream().mapToInt(i -> i).toArray();
    }



}
