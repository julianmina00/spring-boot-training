import java.util.Random;

public class InvoiceGenerator {

  private static final String[] ITEMS = {"Shampoo","Shaving cream","Tooth paste","Deodorant",
      "Soap","Wine","Beer","Orange juice","Milk","Coffee","Apple","Peach","Pineapple","Strawberry",
      "Mango","Olive oil","Butter","Ketchup","Soy sauce","Teriyaki sauce","Bread","Cereals","Oats",
      "Yogurt","Eggs"};
  private static final String ITEMS_SEPARATOR = ",";
  private static Random random = new Random();


  public static void main(String[] args) {
    int invoices;
    try{
      invoices = Integer.parseInt(args[0]);
    }
    catch(Exception ex){
      invoices = 100;
    }

    // Generating invoices
    for (int i = 0; i < invoices ; i++) {
      int amountOfItems = random.nextInt(15);
      if(amountOfItems == 0){
        amountOfItems = 7;
      }
      System.out.println(getInvoiceNumber().concat(":").concat(getTotal()).concat(":").concat(getItems(amountOfItems)));
    }
    //Invoices generated
  }

  private static String getItems(int amount){
    StringBuilder items = new StringBuilder();
    for (int i = 0; i < amount; i++) {
      items.append(ITEMS[random.nextInt(ITEMS.length)]).append(ITEMS_SEPARATOR);
    }
    return items.deleteCharAt(items.lastIndexOf(ITEMS_SEPARATOR)).toString();
  }

  private static String getInvoiceNumber(){
    return Long.toString(Math.abs(random.nextLong()));
  }

  private static String getTotal(){
    String total = Float.toString(100f * random.nextFloat());
    total = total.replace(',', '.');
    return total.substring(0,total.lastIndexOf('.')+3);
  }

}
