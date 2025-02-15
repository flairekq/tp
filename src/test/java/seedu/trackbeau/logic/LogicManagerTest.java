package seedu.trackbeau.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.trackbeau.commons.core.Messages.MESSAGE_INVALID_CUSTOMER_DISPLAYED_INDEX;
import static seedu.trackbeau.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.trackbeau.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.BIRTHDATE_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.trackbeau.logic.commands.CommandTestUtil.REG_DATE_DESC_AMY;
import static seedu.trackbeau.testutil.Assert.assertThrows;
import static seedu.trackbeau.testutil.TypicalCustomers.AMY;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.trackbeau.logic.commands.CommandResult;
import seedu.trackbeau.logic.commands.customer.AddCustomerCommand;
import seedu.trackbeau.logic.commands.customer.DeleteCustomerCommand;
import seedu.trackbeau.logic.commands.customer.ListCustomersCommand;
import seedu.trackbeau.logic.commands.exceptions.CommandException;
import seedu.trackbeau.logic.parser.exceptions.ParseException;
import seedu.trackbeau.model.Model;
import seedu.trackbeau.model.ModelManager;
import seedu.trackbeau.model.ReadOnlyTrackBeau;
import seedu.trackbeau.model.UserPrefs;
import seedu.trackbeau.model.customer.Customer;
import seedu.trackbeau.storage.JsonTrackBeauStorage;
import seedu.trackbeau.storage.JsonUserPrefsStorage;
import seedu.trackbeau.storage.StorageManager;
import seedu.trackbeau.testutil.CustomerBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonTrackBeauStorage trackBeauStorage =
            new JsonTrackBeauStorage(temporaryFolder.resolve("trackbeau.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(trackBeauStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = DeleteCustomerCommand.COMMAND_WORD + " 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_CUSTOMER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCustomersCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCustomersCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonAddressBookIoExceptionThrowingStub
        JsonTrackBeauStorage trackBeauStorage =
            new JsonTrackBeauIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionAddressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
            new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(trackBeauStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Execute add command
        String addCommand = AddCustomerCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
            + ADDRESS_DESC_AMY + BIRTHDATE_DESC_AMY + REG_DATE_DESC_AMY;
        Customer expectedCustomer = new CustomerBuilder(AMY).withStaffs().withServices().withAllergies().build();
        ModelManager expectedModel = new ModelManager();
        expectedModel.addCustomer(expectedCustomer);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredCustomerList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredCustomerList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      Model expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     *
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
        Model expectedModel = new ModelManager(model.getTrackBeau(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     *
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
                                      String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonTrackBeauIoExceptionThrowingStub extends JsonTrackBeauStorage {
        private JsonTrackBeauIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveTrackBeau(ReadOnlyTrackBeau trackBeau, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
