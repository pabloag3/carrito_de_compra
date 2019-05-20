package cliente.bean;

/**
 *
 * @author PabloAgHP
 */
public abstract class BaseEntity<T> extends Entity<T> {

    private boolean isModified;

    /**
     *
     * @param id
     * @param nombre
     */
    public BaseEntity(String nombre) {
        super.nombre = nombre;
        isModified = false;
    }

    /**
     *
     * @return
     */
    public boolean isIsModified() {
        return isModified;
    }

}