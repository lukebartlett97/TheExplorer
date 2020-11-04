package theExplorer.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import theExplorer.ExplorerMod;
import theExplorer.util.AscensionHelper;
import theExplorer.util.MovePicker;

public class Frog extends AbstractMonster {
    public static final String ID = ExplorerMod.makeID(Frog.class.getSimpleName());
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
    private static final int TONGUE_LASH_DMG = 6;
    private static final int TONGUE_LASH_DMG_A = 8;
    private static final int TONGUE_LASH_FRAIL = 1;
    private static final int TONGUE_LASH_FRAIL_A = 2;
    private static final int HOP_DMG = 8;
    private static final int HOP_DMG_A = 10;
    private static final int CROAK_BLOCK = 6;
    private static final int CROAK_BLOCK_A = 8;
    private int tongueLashFrail;
    private int croakBlock;

    // moves
    private static final byte TONGUE_LASH = 1;
    private static final byte HOP = 2;
    private static final byte CROAK = 3;

    public Frog(float offsetX, float offsetY) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, ExplorerMod.makeMonstersPath("Phrog.png"), offsetX, offsetY);
        if (AscensionHelper.tougher(this.type)) {
            this.setHp(HP_MIN_A, HP_MAX_A);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        int tongueLashDmg = AscensionHelper.deadlier(this.type) ? TONGUE_LASH_DMG_A : TONGUE_LASH_DMG;
        int hopDmg = AscensionHelper.deadlier(this.type) ? HOP_DMG_A : HOP_DMG;
        this.tongueLashFrail = AscensionHelper.harder(this.type) ? TONGUE_LASH_FRAIL_A : TONGUE_LASH_FRAIL;
        this.croakBlock = AscensionHelper.harder(this.type) ? CROAK_BLOCK_A : CROAK_BLOCK;
        this.damage.add(new DamageInfo(this, tongueLashDmg));
        this.damage.add(new DamageInfo(this, hopDmg));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case TONGUE_LASH: {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, tongueLashFrail, true)));
                break;
            }
            case HOP: {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
                break;
            }
            case CROAK: {
                addToBot(new GainBlockAction(this, this.croakBlock));
                break;
            }
        }
        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        MovePicker moves = new MovePicker();
        if (!this.lastMove(TONGUE_LASH))
            moves.add(MOVES[0], TONGUE_LASH, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 1.0F);
        if (!this.lastMove(HOP))
            moves.add(MOVES[1], HOP, Intent.ATTACK, this.damage.get(1).base, 1.0F);
        if (!this.lastMove(CROAK))
            moves.add(MOVES[2], CROAK, Intent.DEFEND, croakBlock, 1.0F);
        moves.pickRandomMove(this);
    }
}
