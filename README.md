# News Aggregator Application

A News Search Micro Service, to find relevant news for a particular keyword input from the end user (ex: “apple”) with support for pagination. Apply defaults for the pagination in case the optional inputs are empty or not provided.
Use the APIs from Guardian and New York Times and aggregate the results (eliminating any duplicates) in the output.
1. End user should be able to view results by changing the input parameters
2. The service should be ready to be released to production or live environment
3. The service should be accessible via web browser or postman (using any one of JavaScript frameworks, HTML or JSON)
4. The solution should support offline mode with toggles
5. The service should return relevant results as expected, even while the underlying dependencies (Ex: Public API) are not available!
(Use your own code/logic/data structures and without 3rd party libraries or DB)

API Data Sources
APIs
1. Guardian UK - <a href="https://open-platform.theguardian.com/" target="_blank">https://open-platform.theguardian.com/</a> . Register for a free developer API key. Use the Content Search API.
2. New York Times US - <a href="https://developer.nytimes.com/" target="_blank">https://developer.nytimes.com/</a> . Register for a free developer API key. Use the Article Search API.
