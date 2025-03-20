/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cat.copernic.Entity;

/**
 *
 * @author alpep
 */

@Entity
@Table(name = "Usuari")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Identificador unic per al User. es el email sera la primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String email;


    @Column(nullable = false)
    private String nom;
}
