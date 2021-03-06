package cz.edhouse.javaee.ioc;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Optional;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Frantisek Spacek
 */
@WebServlet(urlPatterns = "/notes")
public class NoteServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(NoteServlet.class.getName());
    
    @Inject
    private NoteManager noteManager;
    

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String id = req.getParameter("id");
        if (id != null) {
            noteManager.delete(Long.valueOf(id));
            return;
        }
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Note noteToUpdate;
        try (Reader reader = req.getReader()) {
            noteToUpdate = Note.fromJson(Json.createReader(reader).readObject());
        } catch (JsonException ex) {
            handleJsonError(ex, resp);
            return;
        }
        noteManager.update(noteToUpdate);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Note noteToCreate;
        try (JsonReader reader = Json.createReader(req.getReader())) {
            noteToCreate = Note.fromJson(reader.readObject());
        } catch (JsonException ex) {
            handleJsonError(ex, resp);
            return;
        }
        noteManager.create(noteToCreate);
        writeJson(resp, Note.toJson(noteToCreate));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String id = req.getParameter("id");
        if (id != null) {
            final Optional<Note> foundNote = noteManager.getOne(Long.valueOf(id));
            if (!foundNote.isPresent()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            } else {
                writeJson(resp, Note.toJson(foundNote.get()));
            }
            return;
        }

        final String ownerId = req.getParameter("ownerId");
        if (ownerId != null) {
            writeJson(resp, Note.toJson(noteManager.getAllForOwner(Long.parseLong(ownerId))));
            return;
        }

        writeJson(resp, Note.toJson(noteManager.getAll()));
    }

    private void writeJson(HttpServletResponse resp, String json) throws IOException {
        try (Writer writer = resp.getWriter()) {
            writer.write(json);
        }
    }

    private void handleJsonError(JsonException ex, HttpServletResponse resp) {
        LOGGER.throwing(NoteServlet.class.getName(), "Invalid json recieved", ex);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
