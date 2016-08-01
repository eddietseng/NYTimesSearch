# NYTimesSearch
NY Times article search databse read-only android application

Time spent: 23 hours spent in total

Completed user stories:

 * [x] User can enter a search query that will display a grid of news articles using the thumbnail and headline from the New York Times Search API.
 * [x] User can click on "settings" which allows selection of advanced search options to filter results.
 * [x] User can configure advanced search filters such as:
 * Begin Date (using a date picker)
 * News desk values (Arts, Fashion & Style, Sports)
 * Sort order (oldest or newest)
 * [x] Subsequent searches will have any filters applied to the search results.
 * [x] User can tap on any article in results to view the contents in an embedded browser.
 * [x] User can scroll down "infinitely" to continue loading more news articles. The maximum number of articles is limited by the API search.

Optional user stories:
 * [x] Robust error handling, check if internet is available, handle error cases, network failures.
 * [x] Use the ActionBar SearchView or custom layout as the query box instead of an EditText.
 * [x] Replace Filter Settings Activity with a lightweight modal overlay.
 * [x] Improve the user interface and experiment with image assets and/or styling and coloring.
 * Added Staggered Grid Spacing.
 * Changed the user input query search text. 
 * Modified RecyclerView item background color.
 * [x] Stretch: Use the RecyclerView with the StaggeredGridLayoutManager to display improve the grid of image results (see Picasso guide too).
 * [x] Stretch: For different news articles that only have text or have text with thumbnails, use Heterogenous Layouts with RecyclerView.
 * [x] Stretch: Apply the popular ButterKnife annotation library to reduce view boilerplate.
 * [x] Stretch: Use Parcelable instead of Serializable using the popular Parceler library.
 * [x] Stretch: Replace Picasso with Glide for more efficient image rendering.
 * [x] Stretch: Switch to using retrolambda expressions to cleanup event handling blocks.


 
The data source in this app is from [*The NY Times API*](http://developer.nytimes.com/).
Special thanks to the *android-async-http*, *Glide*, *Parceler*, *retrolambda* and *butterknife* open-source libraries.

Walkthrough of all user stories:

![Video Walkthrough](NYTimesDemo_API_21.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
