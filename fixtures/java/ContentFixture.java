import jasbro.game.character.CharacterBase;
import jasbro.game.character.CharacterFileLoader;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;

import java.util.List;
import java.util.Map;

/**
 * Generates a GOLDEN CONTENT FIXTURE by running the REAL game's character loader
 * against the real content folder.
 *
 * This is the content-side counterpart to SaveFixture. The C# port's character parser must
 * reproduce exactly what the shipped Java build reads out of the same files.
 *
 * MUST be run with the working directory set to the game folder, because
 * CharacterFileLoader.loadAllCharacters() opens a CWD-RELATIVE path: new TFile("characters").
 *
 * Usage: cd <game folder> && java -cp <game.jar>;<libs>;[outdir] ContentFixture <output.txt>
 */
public class ContentFixture {

    public static void main(String[] args) throws Exception {
        java.io.PrintStream out;
        if (args.length >= 1) {
            out = new java.io.PrintStream(new java.io.FileOutputStream(args[0]), true, "UTF-8");
        } else {
            out = System.out;
        }

        out.println("CWD=" + new java.io.File(".").getCanonicalPath());

        List<CharacterBase> characters = CharacterFileLoader.getInstance().loadAllCharacters(true);
        out.println("COUNT=" + characters.size());

        // Sort by id so the fixture is stable regardless of directory iteration order.
        characters.sort((a, b) -> String.valueOf(a.getId()).compareTo(String.valueOf(b.getId())));

        for (CharacterBase c : characters) {
            out.println("CHAR id=" + c.getId());
            out.println("  name=" + c.getName());
            out.println("  type=" + c.getType());
            out.println("  gender=" + c.getGender());
            out.println("  spec=" + c.getInitialSpecialization());
            out.println("  description=" + c.getDescription());
            out.println("  youngerBase=" + c.getYoungerBase());
            out.println("  olderBase=" + c.getOlderBase());

            // Attributes are an EnumMap of only the keys that were present and > 0.
            StringBuilder attrs = new StringBuilder();
            for (Map.Entry<BaseAttributeTypes, Integer> e : c.getAttributes()) {
                attrs.append(e.getKey()).append('=').append(e.getValue()).append(' ');
            }
            out.println("  attributes=" + attrs.toString().trim());

            StringBuilder traits = new StringBuilder();
            for (Trait t : c.getTraits()) {
                traits.append(t).append(' ');
            }
            out.println("  traits=" + traits.toString().trim());

            out.println("  images=" + c.getImages().size());
            for (ImageData img : c.getImages()) {
                StringBuilder tags = new StringBuilder();
                for (ImageTag t : img.getTags()) {
                    tags.append(t).append(',');
                }
                out.println("    IMG filename=" + img.getFilename()
                        + " tags=" + tags
                        + " customText=" + img.getCustomText());
            }
        }

        out.flush();
        if (out != System.out) {
            out.close();
            System.out.println("wrote " + args[0]);
        }
    }
}
