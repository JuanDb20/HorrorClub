package Model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class HorrorClub {

    private static HorrorClub instance;

    public static HorrorClub getInstance() {
        if (instance == null) {
            instance = new HorrorClub();
        }
        return instance;
    }

    private ArrayList<HorrorMember> members;

    private HorrorClub() {
        this.members = new ArrayList<>();
    }

    public void addMember(HorrorMember member) {
        if (member == null || !member.isValidInfo()) {
            throw new IllegalArgumentException("El miembro es inválido o tiene información incompleta");
        }

        if (member.getId() == null || member.getId().isBlank()) {
            member.setId(UUID.randomUUID().toString());
        }

        for (HorrorMember existing : members) {
            if (Objects.equals(existing.getId(), member.getId())) {
                throw new IllegalArgumentException("El miembro ya está registrado");
            }
        }

        members.add(member);
    }

    public ArrayList<HorrorMember> getMembers() {
        return members;
    }

    public int getSize() {
        return members.size();
    }

    public boolean evaluateMember(String idMember , Evaluation evaluation) {

        for (HorrorMember member : members){
            if(member.getId().equals(idMember)){
                member.setEvaluation(evaluation);
                return true;
            }
        }
        return false;
    }

    public HorrorMember searchById(String id) {

        for (HorrorMember member : members) {
            if (member.getId().equals(id)) {
                return member;
            }
        }
        return null;
    }

    public ArrayList<HorrorMember> filterByLevel(int level) {

        ArrayList<HorrorMember> filtered = new ArrayList<>();

        for (HorrorMember member : members) {
            if (member.getLevelFanaticism() == level) {
                filtered.add(member);
            }
        }
        return filtered;
    }

    public ArrayList<HorrorMember> filterBySubgenre(SubGenre subGenre) {

        ArrayList<HorrorMember> filtered = new ArrayList<>();

        for (HorrorMember member : members) {
            if (member.getFavoriteMovie() != null &&
                    member.getFavoriteMovie().getSubGenre() == subGenre) {
                filtered.add(member);
            }
        }
        return filtered;
    }

    public ArrayList<HorrorMember> getAcceptedMembers() {

        ArrayList<HorrorMember> accepted = new ArrayList<>();

        for (HorrorMember member : members) {
            Evaluation eval = member.getEvaluation();
            if (eval != null && eval.isAccepted()) {
                accepted.add(member);
            }
        }
        return accepted;
    }

    public boolean removeMember(String id) {

        for (int i = 0; i < members.size(); i++) {
            HorrorMember member = members.get(i);
            if (member.getId().equals(id)) {
                members.remove(i);
                return true; // Eliminado correctamente
            }
        }
        return false; // Si no se encontró
    }

    public int numberMembers() {

        int size = members.size();

        return size;
    }
}
