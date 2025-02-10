export default function NewsList({ articles, keyword, timeTaken, currentPage }) {
    return (
        <div className="mb-6">
            <div className="mb-4 text-gray-600">
                Showing results for: <span className="font-semibold">{keyword}</span>
                | Page: {currentPage} | Time Taken: {timeTaken}ms
            </div>
            <div className="grid gap-4">
                {articles.map((article, index) => (
                    <div key={index} className="border p-4 rounded-lg hover:bg-gray-50">
                        <br/>
                        <div className="font-medium text-sm text-gray-500 mb-1">
                            News Source: {article.newsWebsite}
                        </div>
                        <div className="font-medium text-sm text-gray-500 mb-1">
                            Article:
                            <a
                                href={article.url}
                                target="_blank"
                                rel="noopener noreferrer"
                                className="text-lg font-semibold text-blue-600 hover:underline"
                            >
                                 {article.headline}
                            </a>
                        </div>
                        <div className="mt-2 text-sm text-gray-500">
                            Description: {article.description}</div>
                        <div className="mt-2 text-sm text-gray-500">
                            Date Published: {new Date(article.publishedDate).toLocaleDateString()}
                        </div>
                        <br/>
                    </div>
                ))}
            </div>
        </div>
    );
}