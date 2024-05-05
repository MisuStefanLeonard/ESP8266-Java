/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JavaESPControl.Service;

import JavaESPControl.DTOModels.AccountsDTO;
import JavaESPControl.Models.Accounts;
import JavaESPControl.Models.Roles;
import JavaESPControl.Models.UserRoles;
import JavaESPControl.Repository.AccountsRepository;
import JavaESPControl.Repository.RolesRepository;
import JavaESPControl.Repository.UserRolesRepository;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author misustefan
 */
@Service
@Transactional
public class AccountsServiceImplementation implements AccountsService{
    
    private final AccountsRepository accountRepository;
    private final UserRolesRepository userRolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    
    @Autowired
    public AccountsServiceImplementation(AccountsRepository repo,
                                        PasswordEncoder passwordEncoder,
                                        UserRolesRepository userRolesRepository,
                                        RolesRepository rolesRepository){
        accountRepository = repo;
        this.passwordEncoder = passwordEncoder;
        this.userRolesRepository = userRolesRepository;
        this.rolesRepository = rolesRepository;
    }
    
    @Override
    public void saveAccount(AccountsDTO accDTO ){
       
        Roles userRole = rolesRepository.findByroleName("ROLE_USER");
        if(userRole != null){
            Accounts newAccount = new Accounts();
            newAccount.setUsername(accDTO.getName());
            newAccount.setPassword(passwordEncoder.encode(accDTO.getPassword()));
            newAccount.setEmail(accDTO.getEmail());
            // list for the role and account
            List<UserRoles> localList = new LinkedList<UserRoles>();
            // creating newUserRole based on the account saved earlier
            UserRoles newUserRole = new UserRoles();
            newUserRole.setAccount(newAccount);
            newUserRole.setID_ROLE_FK(userRole); 
            // than updating the list for account
            localList.add(newUserRole);
            newAccount.setRolesList(localList);
            // saving them 
            accountRepository.save(newAccount);
            userRolesRepository.save(newUserRole);
        }else{
            throw new RuntimeException("ROLE_USER is not present in the database!");
        }
       

    }
    
    @Override
    public Accounts findByemail(String email){
        return accountRepository.findByemail(email);
    }
}
