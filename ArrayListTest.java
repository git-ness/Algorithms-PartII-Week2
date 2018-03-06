import java.util.ArrayList;

public class ArrayListTest {

    private double[][] energyArray;

    public static void main(String[] args) {

        ArrayListTest arrayListTest = new ArrayListTest();

        ArrayList<String> stringArray = new ArrayList<>();
        stringArray.add("1232");
        stringArray.add("124");
        stringArray.add("124");
        stringArray.add("124");
        stringArray.add("124");
        stringArray.add("124");
        stringArray.add("124");
        stringArray.add("124");

        int indexOf = stringArray.indexOf("124");

        stringArray.set(stringArray.indexOf("124"), "222");


        int placeholder = 0;

        ArrayList<SeamAndEnergy> seamEnergyList = new ArrayList<>();

        seamEnergyList.add(arrayListTest.createSeamEnergy("123"));
        seamEnergyList.add(arrayListTest.createSeamEnergy("321"));
        seamEnergyList.add(arrayListTest.createSeamEnergy("213"));
        seamEnergyList.add(arrayListTest.createSeamEnergy("423"));
        seamEnergyList.add(arrayListTest.createSeamEnergy("412"));
        seamEnergyList.add(arrayListTest.createSeamEnergy("442"));

        for (SeamAndEnergy seamObject : seamEnergyList) {
            if (seamObject.compareTo(seamEnergyList.get(seamEnergyList.indexOf(seamObject.seamString))) == 0) {

            }
        }

        // So I guess this is where I can iterate through the ArrayList<SeamAndEnergy> using a foreach and do something like the below?
        //  MyClass object1 = new MyClass("test");
//        MyClass object2 = new MyClass("test");
//
//        if(object1.compareTo(object2) == 0){
//            System.out.println("true");
//        }
//        else{
//            System.out.println("false");
    }


    private SeamAndEnergy createSeamEnergy(String seamString) {

        return new SeamAndEnergy(seamString);
    }

    private class SeamAndEnergy {
        double energy;
        ArrayList<String> seam;
        private String seamString;

        public SeamAndEnergy(ArrayList<String> seamList) {
            this.seam = seamList;
            addEnergy();
        }

        public SeamAndEnergy(String seamString) {
            this.seamString = seamString;
            addStringEnergy();
        }

        public String getSeamValue(String stringParam) {
            return seamString;
        }

        private void addStringEnergy() {
            String seamString = this.seamString;
            int y = 0;

            for (int x = 0; x < seamString.length(); x++) {

                String[] rowValues = seamString.split("");
                int seamInt = Integer.parseInt(rowValues[x]);
                this.energy += energyArray[seamInt][y];
                int placeholder = 0;
                y++;
            }
        }

        public int compareTo(SeamAndEnergy seamAndEnergy) {
            if (this.seamString.compareTo(seamAndEnergy.seamString) == 0) return 0;
            return 1;
        }

        private void addEnergy() {
            String seamString = seam.get(0);
            int y = 0;

            for (int x = 0; x < seamString.length(); x++) {

                String[] rowValues = seamString.split("");
                int seamInt = Integer.parseInt(rowValues[x]);
                this.energy += energyArray[seamInt][y];
                String placeholder2 = "nothing";
                y++;

            }
        }

        private String getSeamFrom(String seamString) {
            return this.seamString;
        }

    }
}
