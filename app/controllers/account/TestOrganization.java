package controllers.account;

import controllers.Application;
import models.User;
import models.utils.AppException;
import models.utils.Hash;
import models.utils.Mail;
import org.apache.commons.mail.EmailException;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import play.Configuration;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import models.*;
import javax.inject.Inject;
import static play.data.Form.form;
import views.html.account.organization.*;
import javax.inject.Inject;

public class TestOrganization extends Controller {

	private static FormFactory formFactory;

	@Inject
	public TestOrganization(FormFactory formFactory) {
		this.formFactory = formFactory;
	}

	public Result show() {
		Form<Organization> organizationForm = null;

		String email = ctx().session().get("email");
		
		if (email != null) {
			organizationForm = formFactory.form(Organization.class).fill(Organization.findByEmail(email));

		}
		return ok(views.html.account.organization.show.render(organizationForm));
	}

	public Result edit(Long id) {
		Form<Organization> computerForm = formFactory.form(Organization.class).fill(Organization.find.byId(id));
		return ok(views.html.account.organization.editForm.render(id, computerForm));
	}

	public Result update(Long id) throws PersistenceException {
		Form<Organization> organizationForm = formFactory.form(Organization.class).bindFromRequest();
		if (organizationForm.hasErrors()) {
			return badRequest(views.html.account.organization.editForm.render(id, computerForm));
		}

		Transaction txn = Ebean.beginTransaction();
		try {
			Organization savedOrganization = Organization.find.byId(id);
			if (savedOrganization != null) {
				Organization newOrganization = organizationForm.get();
				savedOrganization.name = newOrganization.name;

				savedOrganization.update();
				flash("success", "Organization " + organizationForm.get().name + " has been updated");
				txn.commit();
			}
		} finally {
			txn.end();
		}

		return ok(views.html.account.organization.show.render(organizationForm));
	}

	public Result delete(Long id) {
		Organization.find.ref(id).delete();
		flash("success", "Organization has been deleted");
		return ok(views.html.account.organization.show.render(organizationForm));
	}
}
