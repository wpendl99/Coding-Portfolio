//
//  ViewController.swift
//  WarCardGame
//
//  Created by William Pendleton on 1/1/21.
//  Simple Card Game of Highest Card Wins (Using Random Dealing)
//

import UIKit

class ViewController: UIViewController {

    // In the game, Player is left side, CPU is right side
    // Global Outlets used to bind game images to code
    @IBOutlet weak var leftImageView: UIImageView!
    @IBOutlet weak var rightImageView: UIImageView!
    // Global Outlets used to bind score labels to code
    @IBOutlet weak var leftScoreLabel: UILabel!
    @IBOutlet weak var rightScoreLabel: UILabel!

    // Global variables used to keep track of scores
    var leftScore = 0
    var rightScore = 0


    override func viewDidLoad() {
        // Initial setup
        super.viewDidLoad()
    }


    // Listener used to detect button press
    @IBAction func dealTapped(_ sender: Any) {

        // Get Player Random Card
        let leftNumber = Int.random(in: 2...14)

        // Get CPU Random Card
        let rightNumber = Int.random(in: 2...14)

        // Change card image to Random Card value
        leftImageView.image = UIImage(named: "card\(leftNumber)")
        rightImageView.image = UIImage(named: "card\(rightNumber)")

        // If Player card is higher
        if leftNumber > rightNumber {
            // Player Wins, Add 1 to Player score
            leftScore += 1
            leftScoreLabel.text = String(leftScore)
        }
        // If CPU card is higher
        else if leftNumber < rightNumber {
            // Computer Wins, Add 1 to CPU score
            rightScore += 1
            rightScoreLabel.text = String(rightScore)

        }
        // If tie, do nothing....
        else {
            // tie
        }
    }
}
