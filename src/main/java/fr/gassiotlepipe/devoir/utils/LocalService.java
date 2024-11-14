package fr.gassiotlepipe.devoir.utils;


import fr.gassiotlepipe.devoir.exceptions.ResourceNotFoundException;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;


/**
 * Un service "local" est un service qui fournit des objets de type T1 stockés en dur dans une variable de type List.
 * Ces objets doivent être identifiés de manière unique par une valeur de type T2
 * @param <T1> le type d'objets fournis par ce service local
 * @param <T2> le type de valeur identifiant de manière unique les instances de la classe T2
 */
public abstract class LocalService<T1, T2> {

    protected List<T1> allValues;

    public LocalService() { this.allValues = new ArrayList<>(); }

    public LocalService(List<T1> allValues) {
        this.allValues = allValues;
    }

    /**
     * Cette méthode doit renvoyer le nom de la propriété "identifiante" d'une classe.
     * La propriété identifiante d'une classe est soit une propriété, soit un accesseur qui permet d'identifier de
     * manière unique une instance de cette classe.
     * Exemple :
     *      - Identifiant simple : une instance de la classe Machin est identifiée de manière unique par sa propriété
     *        id, cette méthode doit donc renvoyer "id"
     *
     *      - Identifiant composé : une instance de la classe Chose est identifiée par le couple de propriétés idMachin
     *        et idTruc, et dispose d'un accesseur getIdentifiantUnique qui renvoie cette identification.
     *        Cette méthode devra renvoyer "IdentifiantUnique"
     *
     * @return le nom de la propriété ou de l'accesseur (sans le mot "get") qui identifie de manière unique l'instance
     * d'une classe de type T1
     */
    protected abstract String getIdentifier();


    /**
     * @return la liste de tous les éléments de type T1 contenus par ce service local
     */
    protected List<T1> getAll() {
        return allValues;
    }


    /**
     * Cherche si un élément de type T1 identifié par value existe dans les objets contenus par le service local, et
     * le renvoie le cas échéant
     * @param value objet de type T2 qui identifie peut-être de manière unique une instance de type T1
     * @return l'instance de type T1 si elle est contenue dans la liste d'objets du service local
     * @throws ResourceNotFoundException si aucune instance de type T1 n'a d'identifiant de valeur value
     */
    protected T1 getByIdentifier(T2 value) throws ResourceNotFoundException {
        IndexAndValue<T1> found = this.findByProperty(value);
        return found.value();
    }

    /**
     * Cherche si un objet de type T1 identifié par une valeur de type T2 est bien contenu dans ce service local, et
     * renvoie cet objet et sa position dans la liste du service local le cas échéant
     * @param value la valeur identifiante de l'objet cherché
     * @return un objet contenant l'index de l'objet cherché dans le service local, ainsi que cet objet
     * @throws ResourceNotFoundException
     */
    protected IndexAndValue<T1> findByProperty(T2 value) throws ResourceNotFoundException {
        try {
            boolean found = false;
            int i = 0;
            List<T1> objects = this.getAll();

            // if the list is empty, will throw an exception, which is okay
            Class<?> class_ = objects.get(0).getClass();
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(getIdentifier(), class_);
            while (!found && i < objects.size()) {
                found = propertyDescriptor.getReadMethod().invoke(objects.get(i)) == value;
                i++;
            }
            if (found) {
                return new IndexAndValue<>(i - 1, objects.get(i - 1));
            } else throw new ResourceNotFoundException(class_.getName(), value);
        } catch (Exception e) {
            throw new ResourceNotFoundException("", value);
        }
    }

    /**
     * Un Record permettant de renvoyer l'index d'un objet dans le service local ainsi que cet objet
     * Type de retour uniquement
     * @param index l'index d'un objet dans le service local
     * @param value un objet contenu dans le service local
     */
    protected record IndexAndValue<T1>(int index, T1 value) {}

}
