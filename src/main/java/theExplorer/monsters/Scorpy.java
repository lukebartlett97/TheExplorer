package theExplorer.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import theExplorer.ExplorerMod;
import theExplorer.util.AscensionHelper;
import theExplorer.util.MovePicker;

public class Scorpy extends AbstractMonster {
    public static final String ID = ExplorerMod.makeID(Scorpy.class.getSimpleName());
    public static final String ENCOUNTER_NAME = ID;
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    // location
    private static final float HB_X = 0.0f;
    private static final float HB_Y = -15.0f;
    private static final float HB_W = 200.0F;
    private static final float HB_H = 100.0f;
    // stats
    private static final int HP_MIN = 30;
    private static final int HP_MAX = 35;
    private static final int HP_MIN_A = HP_MIN + 5;
    private static final int HP_MAX_A = HP_MAX + 5;
    private static final int STING_DMG = 6;
    private static final int STING_DMG_A = 8;
    private static final int STING_WOUND_AMT = 1;
    private static final int STING_WOUND_AMT_A = 2;
    private static final int PINCERS_DMG = 4;
    private static final int PINCERS_DMG_A = 6;
    private static final int CURL_ARMOUR = 3;
    private static final int CURL_ARMOUR_A = 4;
    private int stingWoundAmt;
    private int curlArmour;

    // moves
    private static final byte STING = 1;
    private static final byte PINCERS = 2;
    private static final byte CURL = 3;

    public Scorpy(float offsetX, float offsetY) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, offsetX, offsetY);
        if (AscensionHelper.tougher(this.type)) {
            this.setHp(HP_MIN_A, HP_MAX_A);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        int stingDmg = AscensionHelper.deadlier(this.type) ? STING_DMG_A : STING_DMG;
        int pincersDmg = AscensionHelper.deadlier(this.type) ? PINCERS_DMG_A : PINCERS_DMG;
        this.stingWoundAmt = AscensionHelper.harder(this.type) ? STING_WOUND_AMT_A : STING_WOUND_AMT;
        this.curlArmour = AscensionHelper.harder(this.type) ? CURL_ARMOUR_A : CURL_ARMOUR;
        this.damage.add(new DamageInfo(this, stingDmg));
        this.damage.add(new DamageInfo(this, pincersDmg));
        this.loadAnimation("images/monsters/theBottom/slimeS/skeleton.atlas", "images/monsters/theBottom/slimeS/skeleton.json", 1.0F);
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case STING: {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Wound(), stingWoundAmt));
                break;
            }
            case PINCERS: {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case CURL: {
                addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, curlArmour)));
                break;
            }
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        MovePicker moves = new MovePicker();
        if (!this.lastMove(STING))
            moves.add(MOVES[0], STING, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 1.0F);
        if (!this.lastMove(PINCERS))
            moves.add(MOVES[1], PINCERS, Intent.ATTACK, this.damage.get(1).base, 2, true, 1.0F);
        if (!this.lastMove(CURL))
            moves.add(MOVES[2], CURL, Intent.BUFF, 1.0F);
        moves.pickRandomMove(this);
    }
}
