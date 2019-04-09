package com.labs.springbatchsamples.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@ConfigurationProperties(prefix="swagger")
public class SwaggerConfiguration {
	private String versionNumber;
	private String name;
	private String description;
	private String licence;
	private String licenceUrl;
	private String termsUrl;
	private String contactName;
	private String contactUrl;
	private String contactEmail;
	private String packageController;
	private String regexPathMapping;

	@Bean
	public Docket restApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage(packageController))
				.paths(regex(regexPathMapping))
				.build()
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfoBuilder().title(name.toUpperCase())
				.description(description)
				.version(versionNumber)
				.termsOfServiceUrl(termsUrl)
				.contact(new Contact(contactName, contactUrl, contactEmail))
				.license(licence)
				.licenseUrl(licenceUrl)
				.extensions(null).build();
		return apiInfo;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getLicenceUrl() {
		return licenceUrl;
	}

	public void setLicenceUrl(String licenceUrl) {
		this.licenceUrl = licenceUrl;
	}

	public String getTermsUrl() {
		return termsUrl;
	}

	public void setTermsUrl(String termsUrl) {
		this.termsUrl = termsUrl;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactUrl() {
		return contactUrl;
	}

	public void setContactUrl(String contactUrl) {
		this.contactUrl = contactUrl;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getPackageController() {
		return packageController;
	}

	public void setPackageController(String packageController) {
		this.packageController = packageController;
	}

	public String getRegexPathMapping() {
		return regexPathMapping;
	}

	public void setRegexPathMapping(String regexPathMapping) {
		this.regexPathMapping = regexPathMapping;
	}

}
