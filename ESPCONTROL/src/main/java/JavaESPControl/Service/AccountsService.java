/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package JavaESPControl.Service;

import JavaESPControl.DTOModels.AccountsDTO;
import JavaESPControl.Models.Accounts;

/**
 *
 * @author misustefan
 */
public interface AccountsService {
    public void saveAccount(AccountsDTO accountsDTO);
    
    Accounts findByemail(String email);
}
