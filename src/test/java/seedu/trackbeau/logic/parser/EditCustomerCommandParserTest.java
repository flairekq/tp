package seedu.trackbeau.logic.parser;

import static seedu.trackbeau.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.trackbeau.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.ALLERGY_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.ALLERGY_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.HAIR_TYPE_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.HAIR_TYPE_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.trackbeau.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.trackbeau.logic.commands.CommandTestUtil.INVALID_HAIR_TYPE_DESC;
import static seedu.trackbeau.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.trackbeau.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.trackbeau.logic.commands.CommandTestUtil.INVALID_SKIN_TYPE_DESC;
import static seedu.trackbeau.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.SERVICE_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.SERVICE_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.SKIN_TYPE_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.SKIN_TYPE_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.STAFF_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.STAFF_DESC_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_ALLERGY_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_ALLERGY_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_HAIR_TYPE_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_HAIR_TYPE_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_SERVICE_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_SERVICE_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_SKIN_TYPE_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_SKIN_TYPE_BOB;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_STAFF_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.VALID_STAFF_BOB;
import static seedu.trackbeau.logic.parser.CliSyntax.PREFIX_ALLERGIES;
import static seedu.trackbeau.logic.parser.CliSyntax.PREFIX_SERVICES;
import static seedu.trackbeau.logic.parser.CliSyntax.PREFIX_STAFFS;
import static seedu.trackbeau.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.trackbeau.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.trackbeau.testutil.TypicalIndexes.INDEX_FIRST_CUSTOMER;
import static seedu.trackbeau.testutil.TypicalIndexes.INDEX_SECOND_CUSTOMER;
import static seedu.trackbeau.testutil.TypicalIndexes.INDEX_THIRD_CUSTOMER;

import org.junit.jupiter.api.Test;

import seedu.trackbeau.commons.core.index.Index;
import seedu.trackbeau.logic.commands.customer.EditCustomerCommand;
import seedu.trackbeau.logic.commands.customer.EditCustomerCommand.EditCustomerDescriptor;
import seedu.trackbeau.logic.parser.customer.EditCustomerCommandParser;
import seedu.trackbeau.model.customer.Address;
import seedu.trackbeau.model.customer.Email;
import seedu.trackbeau.model.customer.HairType;
import seedu.trackbeau.model.customer.Name;
import seedu.trackbeau.model.customer.Phone;
import seedu.trackbeau.model.customer.SkinType;
import seedu.trackbeau.model.tag.Tag;
import seedu.trackbeau.testutil.EditCustomerDescriptorBuilder;

public class EditCustomerCommandParserTest {

    private static final String STAFFS_EMPTY = " " + PREFIX_STAFFS;
    private static final String SERVICES_EMPTY = " " + PREFIX_SERVICES;
    private static final String ALLERGY_EMPTY = " " + PREFIX_ALLERGIES;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCustomerCommand.MESSAGE_USAGE);

    private EditCustomerCommandParser parser = new EditCustomerCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCustomerCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_CONSTRAINTS); // invalid address
        assertParseFailure(parser, "1" + INVALID_SKIN_TYPE_DESC, SkinType.MESSAGE_CONSTRAINTS); //invalid skin type
        assertParseFailure(parser, "1" + INVALID_HAIR_TYPE_DESC, HairType.MESSAGE_CONSTRAINTS); //invalid hair type
        //no invalid tags because e.g. for staffs stp/ clears the tags in edit, but note : it is invalid in add

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + PHONE_DESC_BOB + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_STAFF} alone will reset the tags of the {@code Customer} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + STAFF_DESC_AMY + STAFF_DESC_BOB + STAFFS_EMPTY, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + STAFF_DESC_AMY + STAFFS_EMPTY + STAFF_DESC_BOB, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1" + STAFFS_EMPTY + STAFF_DESC_AMY + STAFF_DESC_BOB, Tag.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_SERVICE} alone will reset the tags of the {@code Customer} being edited,
        // parsing it together with a valid tag results in error
        assertParseFailure(parser, "1" + SERVICE_DESC_AMY + SERVICE_DESC_BOB
                + SERVICES_EMPTY, Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1" + SERVICE_DESC_AMY
                + SERVICES_EMPTY + SERVICE_DESC_BOB, Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1" + SERVICES_EMPTY
                + SERVICE_DESC_AMY + SERVICE_DESC_BOB, Tag.MESSAGE_CONSTRAINTS);

        // while parsing {@code PREFIX_ALLERGY} alone will reset the tags of the
        // {@code Customer} being edited, parsing it together with a valid tag results in error
        assertParseFailure(parser, "1"
                        + ALLERGY_DESC_BOB + ALLERGY_DESC_AMY + ALLERGY_EMPTY, Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1"
                        + ALLERGY_DESC_BOB + ALLERGY_EMPTY + ALLERGY_DESC_AMY, Tag.MESSAGE_CONSTRAINTS);

        assertParseFailure(parser, "1" + ALLERGY_EMPTY
                + ALLERGY_DESC_BOB + ALLERGY_DESC_AMY, Tag.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC
                        + ADDRESS_DESC_AMY + PHONE_DESC_AMY, Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_CUSTOMER;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + STAFF_DESC_BOB + SKIN_TYPE_DESC_BOB
                + HAIR_TYPE_DESC_BOB + STAFF_DESC_BOB + SERVICE_DESC_BOB + ALLERGY_DESC_BOB
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + STAFF_DESC_AMY + SERVICE_DESC_AMY
                + ALLERGY_DESC_AMY;

        EditCustomerDescriptor descriptor = new EditCustomerDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withSkinType(VALID_SKIN_TYPE_BOB).withHairType(VALID_HAIR_TYPE_BOB)
                .withStaffs(VALID_STAFF_BOB, VALID_STAFF_AMY)
                .withServices(VALID_SERVICE_BOB, VALID_SERVICE_AMY)
                .withAllergies(VALID_ALLERGY_BOB, VALID_ALLERGY_AMY)
                .build();

        EditCustomerCommand expectedCommand = new EditCustomerCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_CUSTOMER;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditCustomerDescriptor descriptor = new EditCustomerDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCustomerCommand expectedCommand = new EditCustomerCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_CUSTOMER;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditCustomerDescriptor descriptor = new EditCustomerDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCustomerCommand expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withAddress(VALID_ADDRESS_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // skin type
        userInput = targetIndex.getOneBased() + SKIN_TYPE_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withSkinType(VALID_SKIN_TYPE_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // hair type
        userInput = targetIndex.getOneBased() + HAIR_TYPE_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withHairType(VALID_HAIR_TYPE_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // staff
        userInput = targetIndex.getOneBased() + STAFF_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withStaffs(VALID_STAFF_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // service
        userInput = targetIndex.getOneBased() + SERVICE_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withServices(VALID_SERVICE_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // allergy
        userInput = targetIndex.getOneBased() + ALLERGY_DESC_AMY;
        descriptor = new EditCustomerDescriptorBuilder().withAllergies(VALID_ALLERGY_AMY).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_CUSTOMER;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + ADDRESS_DESC_AMY + EMAIL_DESC_AMY
                + SKIN_TYPE_DESC_AMY + HAIR_TYPE_DESC_AMY + STAFF_DESC_AMY
                + SERVICE_DESC_AMY + ALLERGY_DESC_AMY
                + PHONE_DESC_BOB + ADDRESS_DESC_BOB + EMAIL_DESC_BOB + SKIN_TYPE_DESC_BOB
                + HAIR_TYPE_DESC_BOB + STAFF_DESC_BOB + SERVICE_DESC_BOB + ALLERGY_DESC_BOB;

        EditCustomerDescriptor descriptor = new EditCustomerDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withSkinType(VALID_SKIN_TYPE_BOB).withHairType(VALID_HAIR_TYPE_BOB)
                .withStaffs(VALID_STAFF_AMY, VALID_STAFF_BOB)
                .withServices(VALID_SERVICE_AMY, VALID_SERVICE_BOB)
                .withAllergies(VALID_ALLERGY_AMY, VALID_ALLERGY_BOB)
                .build();

        EditCustomerCommand expectedCommand = new EditCustomerCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_CUSTOMER;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;
        EditCustomerCommand.EditCustomerDescriptor descriptor = new EditCustomerDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB).build();
        EditCustomerCommand expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + EMAIL_DESC_BOB + INVALID_PHONE_DESC + ADDRESS_DESC_BOB
                + PHONE_DESC_BOB;
        descriptor = new EditCustomerDescriptorBuilder().withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).build();
        expectedCommand = new EditCustomerCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_CUSTOMER;
        String userInput = targetIndex.getOneBased() + STAFFS_EMPTY;

        EditCustomerDescriptor descriptor = new EditCustomerDescriptorBuilder().withStaffs().build();
        EditCustomerCommand expectedCommand = new EditCustomerCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
