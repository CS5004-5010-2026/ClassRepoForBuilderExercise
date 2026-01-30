"""Setup configuration for Connect-N game package."""

from setuptools import setup, find_packages

setup(
    name="connectn",
    version="1.0.0",
    description="A fully-tested Python implementation of Connect-N game with Builder pattern",
    author="CS5004 Teaching Team",
    packages=find_packages(),
    python_requires=">=3.8",
    install_requires=[],
    extras_require={
        "dev": [
            "pytest>=7.4.0",
            "hypothesis>=6.82.0",
            "pytest-cov>=4.1.0",
        ]
    },
    classifiers=[
        "Development Status :: 4 - Beta",
        "Intended Audience :: Education",
        "Programming Language :: Python :: 3",
        "Programming Language :: Python :: 3.8",
        "Programming Language :: Python :: 3.9",
        "Programming Language :: Python :: 3.10",
        "Programming Language :: Python :: 3.11",
    ],
)
