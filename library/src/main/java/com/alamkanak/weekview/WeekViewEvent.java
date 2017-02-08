package com.alamkanak.weekview;

        import android.graphics.Shader;
        import android.support.annotation.ColorInt;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;

        import static com.alamkanak.weekview.WeekViewUtil.*;

/**
 * Created by Raquib-ul-Alam Kanak on 7/21/2014.
 * Website: http://april-shower.com
 */
public class WeekViewEvent {
    private long mId;
    private Calendar mStartTime;
    private Calendar mEndTime;
    private String mName;
    private String mLocation;
    private @ColorInt int mColor;
    private boolean mAllDay;
    private Shader mShader;

    public WeekViewEvent(){

    }

    /**
     * Initializes the event for week view.
     * @param id The id of the event.
     * @param name Name of the event.
     * @param startYear Year when the event starts.
     * @param startMonth Month when the event starts.
     * @param startDay Day when the event starts.
     * @param startHour Hour (in 24-hour format) when the event starts.
     * @param startMinute Minute when the event starts.
     * @param endYear Year when the event ends.
     * @param endMonth Month when the event ends.
     * @param endDay Day when the event ends.
     * @param endHour Hour (in 24-hour format) when the event ends.
     * @param endMinute Minute when the event ends.
     */
    public WeekViewEvent(long id, String name, int startYear, int startMonth, int startDay, int startHour, int startMinute, int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        this.mId = id;

        this.mStartTime = Calendar.getInstance();
        this.mStartTime.set(Calendar.YEAR, startYear);
        this.mStartTime.set(Calendar.MONTH, startMonth-1);
        this.mStartTime.set(Calendar.DAY_OF_MONTH, startDay);
        this.mStartTime.set(Calendar.HOUR_OF_DAY, startHour);
        this.mStartTime.set(Calendar.MINUTE, startMinute);

        this.mEndTime = Calendar.getInstance();
        this.mEndTime.set(Calendar.YEAR, endYear);
        this.mEndTime.set(Calendar.MONTH, endMonth-1);
        this.mEndTime.set(Calendar.DAY_OF_MONTH, endDay);
        this.mEndTime.set(Calendar.HOUR_OF_DAY, endHour);
        this.mEndTime.set(Calendar.MINUTE, endMinute);

        this.mName = name;
    }

    /**
     * Initializes the event for week view.
     * @param id The id of the event.
     * @param name Name of the event.
     * @param location The location of the event.
     * @param startTime The time when the event starts.
     * @param endTime The time when the event ends.
     * @param allDay Is the event an all day event.
     * @param shader the Shader of the event rectangle
     */
    public WeekViewEvent(long id, String name, String location, Calendar startTime, Calendar endTime, boolean allDay, Shader shader) {
        this.mId = id;
        this.mName = name;
        this.mLocation = location;
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mAllDay = allDay;
        this.mShader = shader;
    }

    /**
     * Initializes the event for week view.
     * @param id The id of the event.
     * @param name Name of the event.
     * @param location The location of the event.
     * @param startTime The time when the event starts.
     * @param endTime The time when the event ends.
     * @param allDay Is the event an all day event
     */
    public WeekViewEvent(long id, String name, String location, Calendar startTime, Calendar endTime, boolean allDay) {
        this(id, name, location, startTime, endTime, allDay, null);
    }

    /**
     * Initializes the event for week view.
     * @param id The id of the event.
     * @param name Name of the event.
     * @param location The location of the event.
     * @param startTime The time when the event starts.
     * @param endTime The time when the event ends.
     */
    public WeekViewEvent(long id, String name, String location, Calendar startTime, Calendar endTime) {
        this(id, name, location, startTime, endTime, false);
    }

    /**
     * Initializes the event for week view.
     * @param id The id of the event.
     * @param name Name of the event.
     * @param startTime The time when the event starts.
     * @param endTime The time when the event ends.
     */
    public WeekViewEvent(long id, String name, Calendar startTime, Calendar endTime) {
        this(id, name, null, startTime, endTime);
    }


    public Calendar getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Calendar startTime) {
        this.mStartTime = startTime;
    }

    public Calendar getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Calendar endTime) {
        this.mEndTime = endTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        this.mLocation = location;
    }

    public @ColorInt int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public boolean isAllDay() {
        return mAllDay;
    }

    public void setAllDay(boolean allDay) {
        this.mAllDay = allDay;
    }

    public Shader getShader(){
        return mShader;
    }

    public void setShader(Shader shader){
        mShader = shader;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WeekViewEvent that = (WeekViewEvent) o;

        return mId == that.mId;

    }

    @Override
    public int hashCode() {
        return (int) (mId ^ (mId >>> 32));
    }

    public List<WeekViewEvent> splitWeekViewEvents(){
        return splitWeekViewEvents(0);
    }

    /**
     * Split the events into events used by the Week-View, taking the startHour into account
     * @param startHour The startHour
     * @return The splitted events per day
     */
    public List<WeekViewEvent> splitWeekViewEvents(int startHour){
        //This function splits the WeekViewEvent in WeekViewEvents by day
        List<WeekViewEvent> events = new ArrayList<>();
        // The first millisecond of the next day is still the same day. (no need to split events for this).
        Calendar fixedEndTime = (Calendar) this.getEndTime().clone();
        fixedEndTime.add(Calendar.MILLISECOND, -1);

        Calendar fixedStartTime = (Calendar) this.getStartTime().clone();

        //Events that take all day should only be shown on their exact dates
        //and thus their times should not include the startHour
        if(!isAllDay()) {
            fixedEndTime.add(Calendar.HOUR_OF_DAY, -startHour);
            fixedStartTime.add(Calendar.HOUR, -startHour);
        }

        if (!isSameDay(fixedStartTime, fixedEndTime)) {
            Calendar firstEndTime = (Calendar) this.getStartTime().clone();
            firstEndTime.set(Calendar.HOUR_OF_DAY, 23);
            firstEndTime.set(Calendar.MINUTE, 59);

            WeekViewEvent event1 = new WeekViewEvent(this.getId(), this.getName(), this.getLocation(), fixedStartTime, firstEndTime, this.isAllDay());
            event1.setColor(this.getColor());
            events.add(event1);

            // Add other days.
            //fixedStartTime already takes startHour into account
            Calendar otherDay = (Calendar) fixedStartTime.clone();
            otherDay.add(Calendar.DATE, 1);
            while (!isSameDay(otherDay, fixedEndTime)) {
                Calendar overDay = (Calendar) otherDay.clone();
                overDay.set(Calendar.HOUR_OF_DAY, 0);
                overDay.set(Calendar.MINUTE, 0);

                Calendar endOfOverDay = (Calendar) overDay.clone();
                endOfOverDay.set(Calendar.HOUR_OF_DAY, 23);
                endOfOverDay.set(Calendar.MINUTE, 59);
                WeekViewEvent eventMore = new WeekViewEvent(this.getId(), this.getName(), null, overDay, endOfOverDay, this.isAllDay());
                eventMore.setColor(this.getColor());
                events.add(eventMore);

                // Add next day.
                otherDay.add(Calendar.DATE, 1);
            }

            // Add last day.
            Calendar lastStartTime = (Calendar) fixedEndTime.clone();
            lastStartTime.set(Calendar.HOUR_OF_DAY, 0);
            lastStartTime.set(Calendar.MINUTE, 0);

            Calendar lastEndTime = (Calendar) fixedEndTime.clone();

            WeekViewEvent event2 = new WeekViewEvent(this.getId(), this.getName(), this.getLocation(), lastStartTime, lastEndTime, this.isAllDay());
            event2.setColor(this.getColor());
            events.add(event2);
        } else {
            WeekViewEvent weekViewEvent = new WeekViewEvent(this.getId(), this.getName(), this.getLocation(), fixedStartTime, fixedEndTime, this.isAllDay(), this.getShader());
            weekViewEvent.setColor(getColor());
            events.add(weekViewEvent);
        }

        return events;
    }
}
