package models;

import models.utils.AppException;
import models.utils.Hash;
import play.data.format.Formats;
import play.data.validation.Constraints;
import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Entity
public class Organization extends Model {

	@Id
	public Long id;

	@Constraints.Required
	public String name;

	@Constraints.Required
	@Formats.NonEmpty
	public Long userId;
	
	 public static Finder<Long, Organization> find = new Finder<Long,Organization>(Organization.class);
	 
	// Find all tasks
	 List<Organization> tasks = Organization.find.all();
	 
	 public static Organization findByEmail(String email) {
	        return find.where().eq("email", email).findUnique();
	    }
}
