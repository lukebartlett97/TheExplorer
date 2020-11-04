package theExplorer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theExplorer.ExplorerMod;
import theExplorer.util.PlantService;
import theExplorer.util.TextureLoader;

import static theExplorer.ExplorerMod.makeRelicPath;

public class JarOfDirt extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ExplorerMod.makeID(JarOfDirt.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("jar_of_dirt.png"));

    public JarOfDirt() {
        super(ID, IMG, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw() {
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(PlantService.getRandomPlant(), 1));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(PlantService.getRandomPlant(), 1));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
