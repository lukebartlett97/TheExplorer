package theExplorer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theExplorer.ExplorerMod;
import theExplorer.util.ApplyPowerListener;
import theExplorer.util.TextureLoader;

import static theExplorer.ExplorerMod.makeRelicPath;

public class Painkiller extends CustomRelic implements ApplyPowerListener {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ExplorerMod.makeID(Painkiller.class.getSimpleName());
    public static final int BLOCK = 4;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("painkiller.png"));

    public Painkiller() {
        super(ID, IMG, RelicTier.RARE, LandingSound.FLAT);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target) {
        if(target.isPlayer && power.type == AbstractPower.PowerType.DEBUFF) {
            if(power.amount > 1) {
                power.amount -= 1;
                flash();
            }
            if(power.amount < -1) {
                power.amount += 1;
                flash();
            }
        }
    }
}
