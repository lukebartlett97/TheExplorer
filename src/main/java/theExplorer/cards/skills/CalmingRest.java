package theExplorer.cards.skills;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.cards.AbstractDynamicCard;
import theExplorer.characters.TheExplorer;
import theExplorer.powers.CompanionPower;
import theExplorer.util.CompanionService;

import static theExplorer.ExplorerMod.makeCardPath;

public class CalmingRest extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(CalmingRest.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    //TODO: Add real art.
    public static final String IMG = makeCardPath("Ski_Beta.png");

    public static final String NAME = cardStrings.NAME;

    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int BLOCK = 3;
    private static final int UPGRADE_PLUS_BLOCK = 1;


    // /STAT DECLARATION/


    public CalmingRest() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(CompanionService.getCompanionPower() != null) {
            for(int i = 0; i < CompanionService.getCompanionPower().getTimesActed(); i++) {
                addToBot(new GainBlockAction(p, p, block));
            }
        }
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        initializeDescription();
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = cardStrings.DESCRIPTION;
        if(CompanionService.getCompanionPower() != null) {
            int count = CompanionService.getCompanionPower().getTimesActed();
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0] + count;
            if (count == 1) {
                this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
            } else {
                this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[2];
            }
        }
        super.initializeDescription();
    }
}
