package OOP.Solution;

import OOP.Provided.*;

import java.util.*;
import java.util.stream.Collectors;


public class TechnionTunesImpl implements TechnionTunes {
    private HashMap<Integer, User> users;
    private HashMap<Integer, Song> songs;


    public TechnionTunesImpl() {
        users = new HashMap<>();
        songs = new HashMap<>();
    }

    @Override
    public void addUser(int userID, String userName, int userAge) throws UserAlreadyExists {
        if (users.containsKey(userID))
            throw new UserAlreadyExists();

        users.put(userID, new UserImpl(userID, userName, userAge));
    }

    @Override
    public User getUser(int userID) throws UserDoesntExist {
        if (!users.containsKey(userID))
            throw new UserDoesntExist();

        return users.get(userID);
    }

    @Override
    public void makeFriends(int id1, int id2) throws UserDoesntExist, User.AlreadyFriends, User.SamePerson {
        User user1 = this.getUser(id1);
        User user2 = this.getUser(id2);

        user1.AddFriend(user2);
        user2.AddFriend(user1);

    }

    @Override
    public void addSong(int songID, String songName, int length, String artist) throws SongAlreadyExists {
        if (songs.containsKey(songID))
            throw new SongAlreadyExists();

        songs.put(songID, new SongImpl(songID, songName, length, artist));
    }

    @Override
    public Song getSong(int songID) throws SongDoesntExist {
        if (!songs.containsKey(songID))
            throw new SongDoesntExist();

        return songs.get(songID);
    }

    @Override
    public void rateSong(int userID, int songID, int rate) throws UserDoesntExist, SongDoesntExist, User.IllegalRateValue, User.SongAlreadyRated {
        User user = this.getUser(userID);
        Song song = this.getSong(songID);

        user.rateSong(song, rate);
        song.rateSong(user, rate);
    }

    @Override
    public Set<Song> getIntersection(int IDs[]) throws UserDoesntExist {
        Set<Song> intersection = null;

        for (int id : IDs) {
            User user = this.getUser(id);
            Set<Song> ratedSongs = new HashSet<>(user.getRatedSongs());

            if (intersection == null) {
                intersection = ratedSongs;
            } else {
                intersection.retainAll(ratedSongs);
            }
        }
// TODO: check if this is the correct implementation
//        for (int id : IDs) {
//            User user = this.getUser(id);
//            Set<Song> ratedSongs = new HashSet<>(user.getRatedSongs());
//            intersection.addAll(ratedSongs);
//        }

        return intersection == null ? new HashSet<>() : intersection;
    }

    @Override
    public Collection<Song> sortSongs(Comparator<Song> comp) {
        return songs.values().stream()
                .sorted(comp)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Song> getHighestRatedSongs(int num) {
        return songs.values().stream()
                .sorted(Comparator.comparingDouble(Song::getAverageRating).reversed()
                        .thenComparingInt(Song::getLength).reversed()
                        .thenComparingInt(Song::getID))
                .limit(num)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Song> getMostRatedSongs(int num) {
        return songs.values().stream()
                .sorted(Comparator.comparingInt((Song s) -> s.getRatings().size()).reversed()
                        .thenComparingInt(Song::getLength)
                        .thenComparingInt(Song::getID).reversed())
                .limit(num)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getTopLikers(int num) {
        return users.values().stream()
                .sorted(Comparator.comparingDouble(User::getAverageRating).reversed()
                        .thenComparingInt(User::getAge).reversed()
                        .thenComparingInt(User::getID))
                .limit(num)
                .collect(Collectors.toList());
    }

    @Override
    public boolean canGetAlong(int userId1, int userId2) throws UserDoesntExist {
        User user1 = this.getUser(userId1);
        User user2 = this.getUser(userId2);

        if (user1 == user2)
            return true;

        Set<User> visited = new HashSet<>();
        Queue<User> queue = new LinkedList<>();
        queue.add(user1);
        visited.add(user1);

        while (!queue.isEmpty()) {
            User currentUser = queue.poll();
            for (User friend : currentUser.getFriends().keySet()) {
                if (!visited.contains(friend)) {
                    if (currentUser.favoriteSongInCommon(friend)) {
                        if (friend.equals(user2)) {
                            return true;
                        }
                        queue.add(friend);
                        visited.add(friend);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public Iterator<Song> iterator() {
        return songs.values().stream()
                .sorted(Comparator.comparingInt(Song::getLength)
                        .thenComparingInt(Song::getID))
                .iterator();
    }

}
