
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
enum Character {
    CARTMAN,
    BUTTERS,
    KYLE
}


public class PetModel {
    private int moodMeter, hungryMeter, sleepMeter, cleanMeter, petAge;
    private String nickName;
    private Character petCharacter;
    private boolean alive;
    private int ageLimit;
    private Random rand;
    private ImageIcon house, character1, character2, sleepBackground, bed;
    
    
    public PetModel(String nickname) {
        this(nickname, Character.CARTMAN);
    }
    
    public PetModel(String nickName, Character petCharacter) {
        this.nickName = nickName;
        this.petCharacter  = petCharacter;
        moodMeter = 50;
        hungryMeter = 50;
        sleepMeter = 50;
        cleanMeter = 50;
        petAge = 0;
        alive = true;
        rand = new Random();
        ageLimit = rand.nextInt(20) + 80;
        generateImages();
    }
    
    //method to get the type of character for the pet instance
    public static final Character getCharacterType(String characterName) {
        switch(characterName) {
            case "Cartman":return Character.CARTMAN;
            case "Butters":return Character.BUTTERS;
            default: return Character.KYLE;
        }
    }
    
    public Image getCharacterImage() {
        return character1.getImage();
    }
    
    public Image getCharacterUnhappyImage() {
        return character2.getImage();
    }
    
    
    public Image getSleepingBackgroundImage() {
        return sleepBackground.getImage();
    }
    
    public Image getBedImage() {
        return bed.getImage();
    }

        public final void randomCharacterSelect() { //Generates a random character, used upon new pet creation.
        int temp;
        Character petCharacterRandom = null;
        Random rand = new Random();
        temp = rand.nextInt(3);
        
        switch(temp) {
            case 0:
                petCharacter = Character.BUTTERS;
                break;
            case 1:
                petCharacter = Character.CARTMAN;
                break;
            case 2:
                petCharacter = Character.KYLE;
                break;
        }
    }
    
    //method to get the images from file
    private final void generateImages() {
        house = new ImageIcon("img\\"+getCharacterName()+"\\House.png");
        character1 = new ImageIcon("img\\"+getCharacterName()+"\\Character1.png");
        character2 = new ImageIcon("img\\"+getCharacterName()+"\\Character2.png");
        bed = new ImageIcon("img\\Sleep\\bed.png");
        sleepBackground = new ImageIcon("img\\Sleep\\moon.png");
    }
    
    public Image getBackgroundImage() {
        return house.getImage();
    }
    
    //getters and setters for the pet attributes
    public void setNickname(String nickName) {
    this.nickName = nickName;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public int getAgeLimit() {
        return ageLimit;
    }
    
    public String getNickname() {
        return nickName;
    }
    
    public String getCharacterName() {
        switch(petCharacter) {
            case CARTMAN:
                return "Cartman";
            case BUTTERS:
                return"Butters";
            case KYLE:
                return "Kyle";
            default: return null;
        }
    }
    
    public void setAge(int petAge) {
        this.petAge = petAge;
    }
    
    public int getAge() {
        return petAge;
    }
    
    public int getMoodMeter() {
        return moodMeter;
    }
    
    public void setMoodMeter(int moodMeter) {
        this.moodMeter = moodMeter;
    }
    
    public int getHungryMeter() {
        return hungryMeter;
    }
    
    public void setHungryMeter(int hungryMeter) {
        this.hungryMeter = hungryMeter;
    }
    
    public int getSleepMeter() {
        return sleepMeter;
    }
    
    public void setSleepMeter(int sleepMeter) {
        this.sleepMeter = sleepMeter;
    }
    
    public int getCleanMeter() {
        return cleanMeter;
    }
    
    public boolean isAlive() {
        return !(sleepMeter <= 0 || cleanMeter <= 0 || hungryMeter <= 0 || moodMeter <= 0|| !alive);
    }
    
    public void setCleanMeter(int cleanMeter) {
        this.cleanMeter = cleanMeter;
    }
    
    
    //Text for when the pet is performing actions..
    public String getEatingText() {
                switch(petCharacter) {
            case CARTMAN:
                return "\"Respect mah authoriteh\"";
            case BUTTERS:
                return"\"I'm sad all the time, but I am also happy that something can make me sad.\"";
            case KYLE:
                return "\"All animals kill, and the animals that don't kill are the stupid ones like cows and turtles and stuff.\"";
            default: return null;
        }
    }
    
    //sleeping text for each character
    public String getSleepText() {
        switch(petCharacter) {
            case CARTMAN:
                return "\"Mom! Ben Affleck is naked in my bed!\"";
            case BUTTERS:
                return"\"I don't think I'm very happy. I always fall asleep to the sound of my own screams.\"";
            case KYLE:
                return "\"I hate this town. I really, really do. \"";
            default: return null;
        }
    }
    
    //cleaning text for each character
    public String getCleanText() {
        switch(petCharacter) {
            case CARTMAN:
                return "\"I don't make up the rules, I just think them up and write them down.\"";
            case BUTTERS:
                return"\"Why, i'm aweful disappointed in you\"";
            case KYLE:
                return "\"Does poo go to heaven?\"";
            default: return null;
        }
    }
    
    @Override
    public String toString() {
        return getCharacterName() + ", Age:" + petAge + ", Mood: " + moodMeter + ", Hunger: " + hungryMeter + ", Sleep: " + sleepMeter + ", Clean: " + cleanMeter;
    }
}
