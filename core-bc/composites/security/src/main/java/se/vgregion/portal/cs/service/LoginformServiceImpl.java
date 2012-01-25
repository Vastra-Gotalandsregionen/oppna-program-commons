package se.vgregion.portal.cs.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import se.vgregion.portal.cs.domain.Form;
import se.vgregion.portal.cs.domain.FormField;
import se.vgregion.portal.cs.domain.Option;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static se.vgregion.portal.cs.domain.SelectType.NONE;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-12-27 14:03
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
@Service
public class LoginformServiceImpl implements LoginformService {
    @Override
    public List<Form> extract(Document doc) {
        List<Form> forms = new ArrayList<Form>();

        Elements formElms = doc.getElementsByTag("form");
        for (int i = 0; i < formElms.size(); i++) {
            forms.add(form(formElms.get(i)));
        }
        return forms;
    }

    private Form form(Element formElm) {
        Form form = new Form();
        form.setAction(formElm.attr("action"));
        form.setMethod(formElm.attr("method"));

        List<FormField> formFields = new ArrayList<FormField>();
        // input
        Elements inputElms = formElm.getElementsByTag("input");
        for (Element elm : inputElms) {
            formFields.add(input(elm));
        }

        // select
        Elements selectElms = formElm.getElementsByTag("select");
        for (Element elm : selectElms) {
            formFields.add(select(elm));
        }

        // textarea
        Elements textareaElms = formElm.getElementsByTag("textarea");
        for (Element elm : textareaElms) {
            formFields.add(textarea(elm));
        }

        form.setFormFields(formFields);

        return form;
    }

    private FormField textarea(Element elm) {
        FormField textarea = new FormField();
        textarea.setType(elm.attr("textarea"));
        textarea.setId(elm.attr("id"));
        textarea.setName(elm.attr("name"));
        textarea.setValue(elm.text());
        textarea.setSelectType(NONE);

        return textarea;
    }

    private FormField select(Element selectElm) {
        FormField select = new FormField();
        select.setType("select");
        select.setId(selectElm.attr("id"));
        select.setName(selectElm.attr("name"));

        List<Option> options = new ArrayList<Option>();
        Elements optionElms = selectElm.getElementsByTag("option");
        boolean valueSet = false;
        for (Element elm : optionElms) {
            Option option = new Option();
            option.setDisplay(elm.text());
            option.setValue(elm.attr("value"));
            if (elm.hasAttr("selected")) {
                select.setValue(elm.attr("value"));
                valueSet = true;
            }
        }
        select.setOptions(options);
        if (!valueSet) {
            select.setValue(optionElms.first().attr("value"));
        }
        select.setSelectType(NONE);
        return select;
    }

    private FormField input(Element elm) {
        FormField input = new FormField();
        input.setType(elm.attr("type"));
        input.setId(elm.attr("id"));
        input.setName(elm.attr("name"));
        input.setValue(elm.attr("value"));
        input.setSelectType(NONE);
        return input;
    }
}
