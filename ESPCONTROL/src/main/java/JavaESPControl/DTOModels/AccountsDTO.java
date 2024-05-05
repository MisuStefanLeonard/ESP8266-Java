/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.DTOModels;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author misustefan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountsDTO {
    private Long ID;
    @NotEmpty
    private String userName;
    @NotEmpty(message = "Email should not empty")
    @Email
    private String Email;
    @NotEmpty(message = "Password should not be empty")
    private String Password;
    
    public String getName(){
        return userName;
    }
    public String getEmail(){
        return Email;
    }
    public String getPassowrd(){
        return Password;
    }
   
}
