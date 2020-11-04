package theExplorer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import theExplorer.ExplorerMod;
import theExplorer.powers.ResearchStacksPower;
import theExplorer.powers.ResearchingPower;
import theExplorer.util.TextureLoader;

import static theExplorer.ExplorerMod.logger;
import static theExplorer.ExplorerMod.makeRelicPath;

public class Whistle extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ExplorerMod.makeID(Whistle.class.getSimpleName());
    public static final int BLOCK = 4;

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("whistle.png"));

    public Whistle() {
        super(ID, IMG, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
