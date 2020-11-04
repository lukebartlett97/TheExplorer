package theExplorer;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theExplorer.cards.attacks.*;
import theExplorer.cards.powers.*;
import theExplorer.cards.skills.*;
import theExplorer.characters.TheExplorer;
import theExplorer.potions.AnimalPotion;
import theExplorer.potions.DrowsyPotion;
import theExplorer.potions.TransmogPotion;
import theExplorer.relics.*;
import theExplorer.util.CompanionService;
import theExplorer.util.IDCheckDontTouchPls;
import theExplorer.util.ResearchingService;
import theExplorer.util.TextureLoader;
import theExplorer.variables.DefaultCustomVariable;
import theExplorer.variables.DefaultSecondMagicNumber;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
//TODO: DON'T MASS RENAME/REFACTOR
// Please don't just mass replace "theExplorer" with "yourMod" everywhere.
// It'll be a bigger pain for you. You only need to replace it in 3 places.
// I comment those places below, under the place where you set your ID.

//TODO: FIRST THINGS FIRST: RENAME YOUR PACKAGE AND ID NAMES FIRST-THING!!!
// Right click the package (Open the project pane on the left. Folder with black dot on it. The name's at the very top) -> Refactor -> Rename, and name it whatever you wanna call your mod.
// Scroll down in this file. Change the ID from "theExplorer:" to "yourModName:" or whatever your heart desires (don't use spaces). Dw, you'll see it.
// In the JSON strings (resources>localization>eng>[all them files] make sure they all go "yourModName:" rather than "theExplorer". You can ctrl+R to replace in 1 file, or ctrl+shift+r to mass replace in specific files/directories (Be careful.).
// Start with the DefaultCommon cards - they are the most commented cards since I don't feel it's necessary to put identical comments on every card.
// After you sorta get the hang of how to make cards, check out the card template which will make your life easier

/*
 * With that out of the way:
 * Welcome to this super over-commented Slay the Spire modding base.
 * Use it to make your own mod of any type. - If you want to add any standard in-game content (character,
 * cards, relics), this is a good starting point.
 * It features 1 character with a minimal set of things: 1 card of each type, 1 debuff, couple of relics, etc.
 * If you're new to modding, you basically *need* the BaseMod wiki for whatever you wish to add
 * https://github.com/daviscook477/BaseMod/wiki - work your way through with this base.
 * Feel free to use this in any way you like, of course. MIT licence applies. Happy modding!
 *
 * And pls. Read the comments.
 */

@SpireInitializer
public class ExplorerMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(ExplorerMod.class.getName());
    private static String modID;

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Explorer Mod";
    private static final String AUTHOR = "lukewarm97"; // And pretty soon - You!
    private static final String DESCRIPTION = "A full expansion mod introducing The Explorer.";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown
    public static final Color DROWSY_POTION_LIQUID = CardHelper.getColor(100.0f, 18.0f, 209.0f); // Blue
    public static final Color DROWSY_POTION_SPOTS = CardHelper.getColor(25.0f, 10.0f, 100.0f); // Super Dark Blue

    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!
    // ONCE YOU CHANGE YOUR MOD ID (BELOW, YOU CAN'T MISS IT) CHANGE THESE PATHS!!!!!!!!!!!

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "theExplorerResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "theExplorerResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "theExplorerResources/images/512/bg_power_default_gray.png";

    private static final String ENERGY_ORB_DEFAULT_GRAY = "theExplorerResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "theExplorerResources/images/512/card_small_orb.png";

    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "theExplorerResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "theExplorerResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "theExplorerResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "theExplorerResources/images/1024/card_default_gray_orb.png";

    // Character assets
    private static final String THE_DEFAULT_BUTTON = "theExplorerResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "theExplorerResources/images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "theExplorerResources/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "theExplorerResources/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "theExplorerResources/images/char/defaultCharacter/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "theExplorerResources/images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "theExplorerResources/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "theExplorerResources/images/char/defaultCharacter/skeleton.json";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static String makeMonstersPath(String resourcePath) {
        return getModID() + "Resources/images/monsters/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public ExplorerMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */

        setModID("theExplorer");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:

        // 1. Go to your resources folder in the project panel, and refactor> rename theExplorerResources to
        // yourModIDResources.

        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of theExplorer with yourModID.
        // Because your mod ID isn't the defaultMod. Your cards (and everything else) should have Your mod id. Not mine.

        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than theExplorer. They get loaded before getID is a thing.

        logger.info("Done subscribing");

        logger.info("Creating the color " + TheExplorer.Enums.COLOR_GRAY.toString());

        BaseMod.addColor(TheExplorer.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");


        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");

    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = ExplorerMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO

    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH

    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = ExplorerMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = ExplorerMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO

    // ====== YOU CAN EDIT AGAIN ======


    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        ExplorerMod defaultmod = new ExplorerMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheExplorer.Enums.THE_EXPLORER.toString());

        BaseMod.addCharacter(new TheExplorer("The Explorer", TheExplorer.Enums.THE_EXPLORER),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheExplorer.Enums.THE_EXPLORER);

        receiveEditPotions();
        logger.info("Added " + TheExplorer.Enums.THE_EXPLORER.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {
                }, // thing??????? idk
                (button) -> { // The actual button:

                    enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings);
                        config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        BaseMod.addSaveField("ResearchStacksAmount", new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return ResearchingService.getAmount();
            }

            @Override
            public void onLoad(Integer i) {
                ResearchingService.setAmount(i);
            }
        });

        BaseMod.addSaveField("ResearchStacksKilled", new CustomSavable<String[]>() {
            @Override
            public String[] onSave() {
                return ResearchingService.getKilled();
            }

            @Override
            public void onLoad(String[] strings) {
                ResearchingService.setKilled(strings);
            }
        });

        BaseMod.addSaveField("Companion", new CustomSavable<String>() {
            @Override
            public String onSave() {
                return CompanionService.getCompanionClass();
            }

            @Override
            public void onLoad(String companion) {
                CompanionService.setCompanion(companion);
            }
        });

        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);


        // =============== EVENTS =================

        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        //BaseMod.addEvent(IdentityCrisisEvent.ID, IdentityCrisisEvent.class, TheCity.ID);

        receiveEditMonsters();
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }

    // =============== / POST-INITIALIZE/ =================


    // ================ ADD POTIONS ===================

    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_EXPLORER".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(TransmogPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, TransmogPotion.POTION_ID, TheExplorer.Enums.THE_EXPLORER);
        BaseMod.addPotion(AnimalPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, AnimalPotion.POTION_ID, TheExplorer.Enums.THE_EXPLORER);
        BaseMod.addPotion(DrowsyPotion.class, DROWSY_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, DROWSY_POTION_SPOTS, DrowsyPotion.POTION_ID, TheExplorer.Enums.THE_EXPLORER);
        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        //BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheExplorer.Enums.COLOR_GRAY);
        //BaseMod.addRelicToCustomPool(new BottledPlaceholderRelic(), TheExplorer.Enums.COLOR_GRAY);
        //BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheExplorer.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new JarOfDirt(), TheExplorer.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new Journal(), TheExplorer.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new Leftovers(), TheExplorer.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new Quill(), TheExplorer.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new Whistle(), TheExplorer.Enums.COLOR_GRAY);
        BaseMod.addRelicToCustomPool(new Painkiller(), TheExplorer.Enums.COLOR_GRAY);

        // This adds a relic to the Shared pool. Every character can find this relic.
        //BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        // Mark relics as seen (the others are all starters so they're marked as seen in the character file
        //UnlockTracker.markRelicAsSeen(BottledPlaceholderRelic.ID);
        UnlockTracker.markRelicAsSeen(JarOfDirt.ID);
        UnlockTracker.markRelicAsSeen(Journal.ID);
        UnlockTracker.markRelicAsSeen(Leftovers.ID);
        UnlockTracker.markRelicAsSeen(Quill.ID);
        UnlockTracker.markRelicAsSeen(Whistle.ID);
        UnlockTracker.markRelicAsSeen(Painkiller.ID);
        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        //Ignore this
        pathCheck();
        // Add the Custom Dynamic Variables
        logger.info("Add variables");
        // Add the Custom Dynamic variables
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());

        logger.info("Adding cards");

        //Attacks
        BaseMod.addCard(new BoxOfTricks());
        BaseMod.addCard(new BrambleWeed());
        BaseMod.addCard(new BranchSnap());
        BaseMod.addCard(new Command());
        BaseMod.addCard(new CorruptedBlade());
        BaseMod.addCard(new FindingBalance());
        BaseMod.addCard(new ForestFire());
        BaseMod.addCard(new Fracture());
        BaseMod.addCard(new FrenziedAttack());
        BaseMod.addCard(new HatToss());
        BaseMod.addCard(new Headshot());
        BaseMod.addCard(new Highlander());
        BaseMod.addCard(new Initiative());
        BaseMod.addCard(new Leech());
        BaseMod.addCard(new LuckyStrike());
        BaseMod.addCard(new MoltenBrew());
        BaseMod.addCard(new NutShot());
        BaseMod.addCard(new PiercingShot());
        BaseMod.addCard(new PocketSand());
        BaseMod.addCard(new Rebound());
        BaseMod.addCard(new RockToss());
        BaseMod.addCard(new ShoulderBlow());
        BaseMod.addCard(new ShrapnelShot());
        BaseMod.addCard(new SpudGun());
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new ToePoke());
        BaseMod.addCard(new Trip());
        BaseMod.addCard(new WeakSpot());

        //Skills
        BaseMod.addCard(new AcidRain());
        BaseMod.addCard(new Birdcall());
        BaseMod.addCard(new Brace());
        BaseMod.addCard(new CalmingRest());
        BaseMod.addCard(new Camouflage());
        BaseMod.addCard(new Cleanse());
        BaseMod.addCard(new Decoy());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new DoubleUp());
        BaseMod.addCard(new DuplicAnts());
        BaseMod.addCard(new EmergencySupplies());
        BaseMod.addCard(new Fertilizer());
        BaseMod.addCard(new FollowThrough());
        BaseMod.addCard(new Insight());
        BaseMod.addCard(new Inspect());
        BaseMod.addCard(new InvigoratingBrew());
        BaseMod.addCard(new MedleyBrew());
        BaseMod.addCard(new Outbreak());
        BaseMod.addCard(new Overload());
        BaseMod.addCard(new PicnicBasket());
        BaseMod.addCard(new Regrowth());
        BaseMod.addCard(new Stockpile());
        BaseMod.addCard(new StrangeMushrooms());
        BaseMod.addCard(new StrategicPlanning());
        BaseMod.addCard(new Stun());
        BaseMod.addCard(new SupplyDrop());
        BaseMod.addCard(new SleepPowder());
        BaseMod.addCard(new Tracking());
        BaseMod.addCard(new VenomCoating());
        BaseMod.addCard(new WildOutbreak());

        //Powers
        BaseMod.addCard(new BigBrainForm());
        BaseMod.addCard(new Contagion());
        BaseMod.addCard(new Exhilarate());
        BaseMod.addCard(new FlowerPot());
        BaseMod.addCard(new HedgehogCompanion());
        BaseMod.addCard(new ImprovisedDefenses());
        BaseMod.addCard(new IvyTrap());
        BaseMod.addCard(new MortalTorrent());
        BaseMod.addCard(new OwlCompanion());
        BaseMod.addCard(new Photosynthesis());
        BaseMod.addCard(new PitTrap());
        BaseMod.addCard(new QuestArcaneTech());
        BaseMod.addCard(new QuestFontOfLife());
        BaseMod.addCard(new QuestRepairTower());
        BaseMod.addCard(new TigerCompanion());
        BaseMod.addCard(new TripwireTrap());
        BaseMod.addCard(new TurtleCompanion());

        logger.info("Making sure the cards are unlocked.");
        // Unlock the cards
        // This is so that they are all "seen" in the library, for people who like to look at the card list
        // before playing your mod.

        //Attacks
        UnlockTracker.unlockCard(BoxOfTricks.ID);
        UnlockTracker.unlockCard(BrambleWeed.ID);
        UnlockTracker.unlockCard(BranchSnap.ID);
        UnlockTracker.unlockCard(Command.ID);
        UnlockTracker.unlockCard(CorruptedBlade.ID);
        UnlockTracker.unlockCard(FindingBalance.ID);
        UnlockTracker.unlockCard(ForestFire.ID);
        UnlockTracker.unlockCard(Fracture.ID);
        UnlockTracker.unlockCard(FrenziedAttack.ID);
        UnlockTracker.unlockCard(HatToss.ID);
        UnlockTracker.unlockCard(Headshot.ID);
        UnlockTracker.unlockCard(Highlander.ID);
        UnlockTracker.unlockCard(Initiative.ID);
        UnlockTracker.unlockCard(Leech.ID);
        UnlockTracker.unlockCard(LuckyStrike.ID);
        UnlockTracker.unlockCard(MoltenBrew.ID);
        UnlockTracker.unlockCard(NutShot.ID);
        UnlockTracker.unlockCard(PiercingShot.ID);
        UnlockTracker.unlockCard(PocketSand.ID);
        UnlockTracker.unlockCard(Rebound.ID);
        UnlockTracker.unlockCard(RockToss.ID);
        UnlockTracker.unlockCard(ShoulderBlow.ID);
        UnlockTracker.unlockCard(ShrapnelShot.ID);
        UnlockTracker.unlockCard(SpudGun.ID);
        UnlockTracker.unlockCard(Strike.ID);
        UnlockTracker.unlockCard(ToePoke.ID);
        UnlockTracker.unlockCard(Trip.ID);
        UnlockTracker.unlockCard(WeakSpot.ID);

        //Skills
        UnlockTracker.unlockCard(AcidRain.ID);
        UnlockTracker.unlockCard(Birdcall.ID);
        UnlockTracker.unlockCard(Brace.ID);
        UnlockTracker.unlockCard(CalmingRest.ID);
        UnlockTracker.unlockCard(Camouflage.ID);
        UnlockTracker.unlockCard(Cleanse.ID);
        UnlockTracker.unlockCard(Decoy.ID);
        UnlockTracker.unlockCard(Defend.ID);
        UnlockTracker.unlockCard(DoubleUp.ID);
        UnlockTracker.unlockCard(DuplicAnts.ID);
        UnlockTracker.unlockCard(EmergencySupplies.ID);
        UnlockTracker.unlockCard(Fertilizer.ID);
        UnlockTracker.unlockCard(FollowThrough.ID);
        UnlockTracker.unlockCard(Insight.ID);
        UnlockTracker.unlockCard(Inspect.ID);
        UnlockTracker.unlockCard(InvigoratingBrew.ID);
        UnlockTracker.unlockCard(MedleyBrew.ID);
        UnlockTracker.unlockCard(Outbreak.ID);
        UnlockTracker.unlockCard(Overload.ID);
        UnlockTracker.unlockCard(PicnicBasket.ID);
        UnlockTracker.unlockCard(Regrowth.ID);
        UnlockTracker.unlockCard(Stockpile.ID);
        UnlockTracker.unlockCard(StrangeMushrooms.ID);
        UnlockTracker.unlockCard(StrategicPlanning.ID);
        UnlockTracker.unlockCard(Stun.ID);
        UnlockTracker.unlockCard(SupplyDrop.ID);
        UnlockTracker.unlockCard(SleepPowder.ID);
        UnlockTracker.unlockCard(Tracking.ID);
        UnlockTracker.unlockCard(VenomCoating.ID);
        UnlockTracker.unlockCard(WildOutbreak.ID);

        //Powers
        UnlockTracker.unlockCard(BigBrainForm.ID);
        UnlockTracker.unlockCard(Contagion.ID);
        UnlockTracker.unlockCard(Exhilarate.ID);
        UnlockTracker.unlockCard(FlowerPot.ID);
        UnlockTracker.unlockCard(HedgehogCompanion.ID);
        UnlockTracker.unlockCard(ImprovisedDefenses.ID);
        UnlockTracker.unlockCard(IvyTrap.ID);
        UnlockTracker.unlockCard(MortalTorrent.ID);
        UnlockTracker.unlockCard(OwlCompanion.ID);
        UnlockTracker.unlockCard(Photosynthesis.ID);
        UnlockTracker.unlockCard(PitTrap.ID);
        UnlockTracker.unlockCard(QuestArcaneTech.ID);
        UnlockTracker.unlockCard(QuestFontOfLife.ID);
        UnlockTracker.unlockCard(QuestRepairTower.ID);
        UnlockTracker.unlockCard(TigerCompanion.ID);
        UnlockTracker.unlockCard(TripwireTrap.ID);
        UnlockTracker.unlockCard(TurtleCompanion.ID);

        logger.info("Done adding cards!");
    }

    // There are better ways to do this than listing every single individual card, but I do not want to complicate things
    // in a "tutorial" mod. This will do and it's completely ok to use. If you ever want to clean up and
    // shorten all the imports, go look take a look at other mods, such as Hubris.

    // ================ /ADD CARDS/ ===================

    public void receiveEditMonsters() {
//        BaseMod.addMonster(Frog.ENCOUNTER_NAME, Frog.NAME, () -> new MonsterGroup(
//                new AbstractMonster[] {new Frog(-300.0f, 0.0f), new Frog(0.0f, 0.0f), new Frog(300.0f, 0.0f) }));
//        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(Frog.ENCOUNTER_NAME, 200.0f));
//
//        BaseMod.addMonster(Snek.ENCOUNTER_NAME, Snek.NAME, () -> new MonsterGroup(
//                new AbstractMonster[] {new Snek(-300.0f, 0.0f), new Snek(0.0f, 0.0f), new Snek(300.0f, 0.0f) }));
//        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(Snek.ENCOUNTER_NAME, 200.0f));
//
//        BaseMod.addMonster(Scorpy.ENCOUNTER_NAME, Scorpy.NAME, () -> new MonsterGroup(
//                new AbstractMonster[] {new Scorpy(-300.0f, 0.0f), new Scorpy(0.0f, 0.0f), new Scorpy(300.0f, 0.0f) }));
//        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(Scorpy.ENCOUNTER_NAME, 200.0f));
//
//        BaseMod.addMonster(Box.ENCOUNTER_NAME, Box.NAME, () -> new MonsterGroup(
//                new AbstractMonster[] {new Box(0.0f, 0.0f)}));
//        BaseMod.addMonsterEncounter(Exordium.ID, new MonsterInfo(Box.ENCOUNTER_NAME, 200.0f));
    }


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Character-Strings.json");

        // OrbStrings
        BaseMod.loadCustomStringsFile(OrbStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Orb-Strings.json");

        // MonsterStrings
        BaseMod.loadCustomStringsFile(MonsterStrings.class,
                getModID() + "Resources/localization/eng/ExplorerMod-Monster-Strings.json");

        logger.info("Done editing strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/ExplorerMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
