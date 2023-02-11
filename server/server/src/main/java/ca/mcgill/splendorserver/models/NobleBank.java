package ca.mcgill.splendorserver.models;

public class NobleBank {
    private Noble[] nobles;
    private int size;

    public NobleBank(int size) {
        this.nobles = new Noble[size];
        this.size = size;
    }

    public boolean add(Noble noble){
        for(int i = 0; i < size; i++) {
            if(nobles[i] == null){
                nobles[i] = noble;
                return true;
            }
        }
        return false;
    }

    public boolean remove(Noble noble){
        for(int i = 0; i < size; i++){
            if(noble.getId() == nobles[i].getId()){
                nobles[i] = null;
                return true;
            }
        }
        return false;
    }

    public Noble[] getNobles() {
        return nobles;
    }


}
