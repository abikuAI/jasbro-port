package jasbro.texts;

import bsh.EvalError;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.events.business.CustomerGroup;
import jasbro.game.interfaces.AttributeType;
import jasbro.game.interfaces.Person;
import jasbro.game.items.Inventory;
import jasbro.game.items.Item;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stringtemplate.v4.ST;

public class TextUtil {
   private static final Logger log = LogManager.getLogger(TextUtil.class);
   private static TextUtil instance;
   private ResourceBundle resourceBundle;
   private Locale locale = Locale.getDefault();

   private TextUtil() {
   }

   public static TextUtil getInstance() {
      if (instance == null) {
         instance = new TextUtil();
      }

      return instance;
   }

   public static boolean containsKey(String key) {
      return getInstance().getResourceBundle().containsKey(key);
   }

   public static String t(String key) {
      return getInstance().getText(key, true);
   }

   public static String tNoCheck(String key) {
      return getInstance().getTextNoCheck(key, null, (Object[])null);
   }

   public static String t(String key, Person person) {
      return getInstance().getText(key, (Object[])null, person);
   }

   public static String t(String key, Person character1, Person character2) {
      return getInstance().getText(key, (Object[])null, character1, character2);
   }

   public static String t(String key, Person character1, Person character2, boolean error) {
      List<Person> people = new ArrayList<>();
      people.add(character1);
      people.add(character2);
      return getInstance().getText(key, people, (Object[])null, error);
   }

   public static String t(String key, Person character1, Person character2, Object... arguments) {
      return getInstance().getText(key, arguments, character1, character2);
   }

   public static String t(String key, List<? extends Person> characters) {
      return getInstance().getText(key, characters, (Object[])null);
   }

   public static String t(String key, Person[] characters, Object... arguments) {
      return getInstance().getText(key, new ArrayList<>(Arrays.asList(characters)), arguments);
   }

   public static String t(String key, List<? extends Person> characters, Object... arguments) {
      return getInstance().getText(key, characters, arguments);
   }

   public static String t(String key, Person person, Object... arguments) {
      return getInstance().getText(key, arguments, person);
   }

   public static String t(String key, Object... arguments) {
      return getInstance().getText(key, null, arguments);
   }

   private String getText(String key, boolean error) {
      try {
         if (this.getResourceBundle().containsKey(key)) {
            return this.getResourceBundle().getString(key).trim();
         } else if (error) {
            log.error("Key not found: {}", new Object[]{key});
            return key;
         } else {
            return null;
         }
      } catch (Exception e) {
         log.error("Error while loading text {}", new Object[]{key});
         log.throwing(e);
         return key;
      }
   }

   private String getTextNoCheck(String key, List<Person> people, Object... arguments) {
      String text = this.getText(key, people, arguments);
      return key.equals(text) ? null : text;
   }

   private String getText(String key, Object[] arguments, Person... people) {
      List<Person> peopleList = Arrays.asList(people);
      return this.getText(key, peopleList, arguments);
   }

   private String getText(String key, List<? extends Person> people, Object... arguments) {
      return this.getText(key, people, true, arguments);
   }

   private String getText(String key, List<? extends Person> people, boolean error, Object... arguments) {
      if (people != null && people.size() > 1 && this.getResourceBundle().containsKey(key + ".group")) {
         key = key + ".group";
      } else if (!this.getResourceBundle().containsKey(key) && people != null && people.size() > 0) {
         if (this.getResourceBundle().containsKey(key + ".female")) {
            if (people.get(0).getGender() == Gender.MALE) {
               key = key + ".male";
            } else if (people.get(0).getGender() == Gender.FUTA && this.getResourceBundle().containsKey(key + ".futa")) {
               key = key + ".futa";
            } else {
               key = key + ".female";
            }
         } else if (people.size() > 1 && this.getResourceBundle().containsKey(key + ".female2")) {
            if (people.get(1) instanceof CustomerGroup && this.getResourceBundle().containsKey(key + ".group2")) {
               key = key + ".group2";
            } else if (people.get(1).getGender() == Gender.MALE) {
               key = key + ".male2";
            } else if (people.get(1).getGender() == Gender.FUTA && this.getResourceBundle().containsKey(key + ".futa2")) {
               key = key + ".futa2";
            } else {
               key = key + ".female2";
            }
         } else if (people.size() > 0 && people.get(0) instanceof Charakter && this.getResourceBundle().containsKey(key + ".trainer")) {
            if (((Charakter)people.get(0)).getType() == CharacterType.TRAINER) {
               key = key + ".trainer";
            } else {
               key = key + ".slave";
            }
         }
      }

      if (this.getResourceBundle().containsKey(key)) {
         String text = this.getText(key, error);
         if (text == null) {
            return null;
         }

         if (people != null) {
            text = this.applyTemplates(text, people);
         }

         if (arguments != null) {
            text = MessageFormat.format(text, arguments);
         }

         return text;
      } else {
         return key;
      }
   }

   public String insertObjects(String text, Object... arguments) {
      return MessageFormat.format(text, arguments);
   }

   public String applyTemplates(String text, List<? extends Person> people) {
      ST stringTemplate = new ST(text);

      for (int i = 0; i < people.size(); i++) {
         Person person = people.get(i);
         if (person != null) {
            TextWrapper textWrapper = new TextWrapper(person);
            if (i == 0) {
               stringTemplate.add("c", textWrapper);
            }

            stringTemplate.add("c" + (i + 1), textWrapper);
         }
      }

      if (people.size() > 1) {
         stringTemplate.add("characters", listCharacters(people));
      } else if (people.size() > 0 && people.get(0) != null) {
         stringTemplate.add("characters", people.get(0).getName());
      }

      return stringTemplate.render();
   }

   public String applyTemplates(String text, List<? extends Person> people, Map<String, Object> variableMap) throws EvalError {
      Set<String> variables = variableMap.keySet();
      ST stringTemplate = new ST(text);

      for (String variable : variables) {
         stringTemplate.add(variable, variableMap.get(variable));
      }

      if (people != null) {
         for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            if (person != null) {
               TextWrapper textWrapper = new TextWrapper(person);
               if (i == 0) {
                  stringTemplate.add("c", textWrapper);
               }

               stringTemplate.add("c" + (i + 1), textWrapper);
            }
         }

         if (people.size() > 1) {
            stringTemplate.add("characters", listCharacters(people));
         } else if (people.size() > 0 && people.get(0) != null) {
            stringTemplate.add("characters", people.get(0).getName());
         }
      }

      return stringTemplate.render();
   }

   private ResourceBundle getResourceBundle() {
      if (this.resourceBundle == null) {
         this.resourceBundle = ResourceBundle.getBundle("jasbro", this.locale);
      }

      return this.resourceBundle;
   }

   public static String listCharacters(List<? extends Person> characters) {
      String retval = "";

      for (int i = 0; i < characters.size(); i++) {
         retval = retval + characters.get(i).getName();
         if (i < characters.size() - 1) {
            if (i == characters.size() - 2) {
               retval = retval + " " + t("and") + " ";
            } else {
               retval = retval + ", ";
            }
         }
      }

      return retval;
   }

   public static String listAttributes(List<AttributeType> attributes) {
      List<String> stringList = new ArrayList<>();

      for (AttributeType attribute : attributes) {
         stringList.add(attribute.getText());
      }

      return listStrings(stringList);
   }

   public static String listStrings(List<String> strings) {
      StringBuilder retval = new StringBuilder();

      for (int i = 0; i < strings.size(); i++) {
         retval.append(strings.get(i));
         if (i < strings.size() - 1) {
            if (i == strings.size() - 2) {
               retval.append(" ").append(t("and")).append(" ");
            } else {
               retval.append(", ");
            }
         }
      }

      return retval.toString();
   }

   public static String listItems(List<Inventory.ItemData> items) {
      String retval = "";

      for (int i = 0; i < items.size(); i++) {
         Inventory.ItemData itemData = items.get(i);
         Object[] arguments = new Object[]{itemData.getAmount(), itemData.getItem().getName()};
         if (itemData.getAmount() != 1) {
            retval = retval + t("itemList.multiple", arguments);
         } else {
            retval = retval + t("itemList.single", arguments);
         }

         if (i < items.size() - 1) {
            if (i == items.size() - 2) {
               retval = retval + " " + t("and") + " ";
            } else {
               retval = retval + ", ";
            }
         }
      }

      return retval;
   }

   public static String html(String text) {
      return "<html>" + text + "</html>";
   }

   public static String htmlPreformatted(String text) {
      return "<html><pre style=\"white-space: pre-wrap; word-wrap: break-word;\"><font face=\"Tahoma\">" + text + "</font></pre></html>";
   }

   public static String htmlItem(Item item) {
      String text = "<html>";
      return text
         + "<pre style=\"width: 350px; max-width: 350px; white-space: pre-wrap; word-wrap: break-word;\"><font face=\"Tahoma\">"
         + item.getText()
         + "</font>"
         + "</pre>"
         + "</html>";
   }

   public static String firstCharUpper(String text) {
      return (text.charAt(0) + "").toUpperCase() + text.substring(1, text.length());
   }
}
