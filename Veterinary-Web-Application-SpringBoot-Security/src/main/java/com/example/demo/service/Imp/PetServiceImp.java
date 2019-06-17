package com.example.demo.service.Imp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.Customer;
import com.example.demo.model.Pet;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PetRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.PetService;

@Service
public class PetServiceImp implements PetService{

	
	public final CustomerRepository customerRepository;
	public final PetRepository petRepository;
	
	public PetServiceImp(CustomerRepository customerRepository,PetRepository petRepository) {
		this.customerRepository=customerRepository;
		this.petRepository=petRepository;
	}
	
	public Page<Customer> GetAllPagination(Pageable pageable){
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        
        Page<Customer> page=customerRepository.findAll(PageRequest.of(pageable.getPageNumber(), 
				  pageable.getPageSize(), 
				  Sort.by(Sort.Direction.ASC, "customerid")));
        return page;
	}
	
	public List<Pet> findAll(){
		return petRepository.findAll();
	}
	
	public List<Pet> findByCustomer(Customer customer){
		return petRepository.findByCustomer(customer);
	}

	public Boolean save(Pet pet) {
		try {
			petRepository.save(pet);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public Optional<Pet> findById(Long id){
		return petRepository.findById(id);
	}
	public Boolean delete(Pet entity) {
		try {
			entity.setCustomer(null);
			petRepository.delete(entity);
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
