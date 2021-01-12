/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author devve
 */
public class Administrator extends User {

    public Administrator() {
        super("Administrator");
    }

    @Override
    public String privledgeLevel() {
        return "Administrator Privledges";
    }

}
