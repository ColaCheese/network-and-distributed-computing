package com.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.webservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RemoveAll_QNAME = new QName("http://www.webservice.com", "removeAll");
    private final static QName _RemoveProjectResponse_QNAME = new QName("http://www.webservice.com", "removeProjectResponse");
    private final static QName _AddProjects_QNAME = new QName("http://www.webservice.com", "addProjects");
    private final static QName _QuaryMeeting_QNAME = new QName("http://www.webservice.com", "quaryMeeting");
    private final static QName _RemoveAllResponse_QNAME = new QName("http://www.webservice.com", "removeAllResponse");
    private final static QName _ParseException_QNAME = new QName("http://www.webservice.com", "ParseException");
    private final static QName _AddProjectsResponse_QNAME = new QName("http://www.webservice.com", "addProjectsResponse");
    private final static QName _RemoveProject_QNAME = new QName("http://www.webservice.com", "removeProject");
    private final static QName _AddPersonResponse_QNAME = new QName("http://www.webservice.com", "addPersonResponse");
    private final static QName _LoginResponse_QNAME = new QName("http://www.webservice.com", "loginResponse");
    private final static QName _DisplayProjectResponse_QNAME = new QName("http://www.webservice.com", "displayProjectResponse");
    private final static QName _AddPerson_QNAME = new QName("http://www.webservice.com", "addPerson");
    private final static QName _Login_QNAME = new QName("http://www.webservice.com", "login");
    private final static QName _QuaryMeetingResponse_QNAME = new QName("http://www.webservice.com", "quaryMeetingResponse");
    private final static QName _DisplayProject_QNAME = new QName("http://www.webservice.com", "displayProject");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.webservice
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddPersonResponse }
     *
     */
    public AddPersonResponse createAddPersonResponse() {
        return new AddPersonResponse();
    }

    /**
     * Create an instance of {@link DisplayProjectResponse }
     *
     */
    public DisplayProjectResponse createDisplayProjectResponse() {
        return new DisplayProjectResponse();
    }

    /**
     * Create an instance of {@link LoginResponse }
     *
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link AddProjectsResponse }
     *
     */
    public AddProjectsResponse createAddProjectsResponse() {
        return new AddProjectsResponse();
    }

    /**
     * Create an instance of {@link RemoveProject }
     *
     */
    public RemoveProject createRemoveProject() {
        return new RemoveProject();
    }

    /**
     * Create an instance of {@link QuaryMeetingResponse }
     *
     */
    public QuaryMeetingResponse createQuaryMeetingResponse() {
        return new QuaryMeetingResponse();
    }

    /**
     * Create an instance of {@link DisplayProject }
     *
     */
    public DisplayProject createDisplayProject() {
        return new DisplayProject();
    }

    /**
     * Create an instance of {@link Login }
     *
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link AddPerson }
     *
     */
    public AddPerson createAddPerson() {
        return new AddPerson();
    }

    /**
     * Create an instance of {@link RemoveAll }
     *
     */
    public RemoveAll createRemoveAll() {
        return new RemoveAll();
    }

    /**
     * Create an instance of {@link RemoveProjectResponse }
     *
     */
    public RemoveProjectResponse createRemoveProjectResponse() {
        return new RemoveProjectResponse();
    }

    /**
     * Create an instance of {@link ParseException }
     *
     */
    public ParseException createParseException() {
        return new ParseException();
    }

    /**
     * Create an instance of {@link AddProjects }
     *
     */
    public AddProjects createAddProjects() {
        return new AddProjects();
    }

    /**
     * Create an instance of {@link QuaryMeeting }
     *
     */
    public QuaryMeeting createQuaryMeeting() {
        return new QuaryMeeting();
    }

    /**
     * Create an instance of {@link RemoveAllResponse }
     *
     */
    public RemoveAllResponse createRemoveAllResponse() {
        return new RemoveAllResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveAll }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "removeAll")
    public JAXBElement<RemoveAll> createRemoveAll(RemoveAll value) {
        return new JAXBElement<RemoveAll>(_RemoveAll_QNAME, RemoveAll.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveProjectResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "removeProjectResponse")
    public JAXBElement<RemoveProjectResponse> createRemoveProjectResponse(RemoveProjectResponse value) {
        return new JAXBElement<RemoveProjectResponse>(_RemoveProjectResponse_QNAME, RemoveProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddProjects }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "addProjects")
    public JAXBElement<AddProjects> createAddProjects(AddProjects value) {
        return new JAXBElement<AddProjects>(_AddProjects_QNAME, AddProjects.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QuaryMeeting }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "quaryMeeting")
    public JAXBElement<QuaryMeeting> createQuaryMeeting(QuaryMeeting value) {
        return new JAXBElement<QuaryMeeting>(_QuaryMeeting_QNAME, QuaryMeeting.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveAllResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "removeAllResponse")
    public JAXBElement<RemoveAllResponse> createRemoveAllResponse(RemoveAllResponse value) {
        return new JAXBElement<RemoveAllResponse>(_RemoveAllResponse_QNAME, RemoveAllResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ParseException }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "ParseException")
    public JAXBElement<ParseException> createParseException(ParseException value) {
        return new JAXBElement<ParseException>(_ParseException_QNAME, ParseException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddProjectsResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "addProjectsResponse")
    public JAXBElement<AddProjectsResponse> createAddProjectsResponse(AddProjectsResponse value) {
        return new JAXBElement<AddProjectsResponse>(_AddProjectsResponse_QNAME, AddProjectsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RemoveProject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "removeProject")
    public JAXBElement<RemoveProject> createRemoveProject(RemoveProject value) {
        return new JAXBElement<RemoveProject>(_RemoveProject_QNAME, RemoveProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddPersonResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "addPersonResponse")
    public JAXBElement<AddPersonResponse> createAddPersonResponse(AddPersonResponse value) {
        return new JAXBElement<AddPersonResponse>(_AddPersonResponse_QNAME, AddPersonResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DisplayProjectResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "displayProjectResponse")
    public JAXBElement<DisplayProjectResponse> createDisplayProjectResponse(DisplayProjectResponse value) {
        return new JAXBElement<DisplayProjectResponse>(_DisplayProjectResponse_QNAME, DisplayProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddPerson }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "addPerson")
    public JAXBElement<AddPerson> createAddPerson(AddPerson value) {
        return new JAXBElement<AddPerson>(_AddPerson_QNAME, AddPerson.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QuaryMeetingResponse }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "quaryMeetingResponse")
    public JAXBElement<QuaryMeetingResponse> createQuaryMeetingResponse(QuaryMeetingResponse value) {
        return new JAXBElement<QuaryMeetingResponse>(_QuaryMeetingResponse_QNAME, QuaryMeetingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DisplayProject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.webservice.com", name = "displayProject")
    public JAXBElement<DisplayProject> createDisplayProject(DisplayProject value) {
        return new JAXBElement<DisplayProject>(_DisplayProject_QNAME, DisplayProject.class, null, value);
    }

}