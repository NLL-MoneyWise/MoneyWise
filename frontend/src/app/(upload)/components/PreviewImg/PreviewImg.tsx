import React from 'react';

interface PreviewImg {
    previewUrls: string[];
    handleDeleteFile: (index: number) => void;
}

const PreviewImg = ({ previewUrls, handleDeleteFile }: PreviewImg) => {
    return (
        <div>
            {previewUrls.length > 0 && (
                <div className="mt-6">
                    <h2 className="text-lg text-gray-700 font-semibold mb-3">
                        미리보기 ({previewUrls.length}개 파일)
                    </h2>
                    <div className="grid grid-cols-3 gap-4 max-h-96 overflow-y-auto">
                        {previewUrls.map((url, index) => (
                            <div key={`${url}-${index}`} className="relative">
                                <img
                                    src={url}
                                    alt={`Receipt preview ${index + 1}`}
                                    className="w-full h-40 object-cover rounded-lg shadow-sm border border-gray-200"
                                />
                                <button
                                    type="button"
                                    onClick={() => handleDeleteFile(index)}
                                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full w-6 h-6 flex items-center justify-center hover:bg-red-600"
                                >
                                    ×
                                </button>
                                <span className="absolute bottom-2 left-2 bg-gray-800 bg-opacity-75 text-white text-xs px-2 py-1 rounded">
                                    {index + 1}
                                </span>
                            </div>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
};

export default PreviewImg;
